package cz.cvut.kbss.reporting.service;

import cz.cvut.kbss.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.exception.UnsupportedReportTypeException;
import cz.cvut.kbss.reporting.filter.ReportFilter;
import cz.cvut.kbss.reporting.model.*;
import cz.cvut.kbss.reporting.model.util.EntityToOwlClassMapper;
import cz.cvut.kbss.reporting.model.util.ReportLastModifiedComparator;
import cz.cvut.kbss.reporting.persistence.dao.ReportDao;
import cz.cvut.kbss.reporting.service.data.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
public class MainReportService implements ReportBusinessService {

    private final ReportDao reportDao;

    private final OccurrenceReportService occurrenceReportService;

    private final AttachmentService attachmentService;

    private final Map<String, Class<? extends LogicalDocument>> entitiesToOwlClasses = new HashMap<>();

    private final Map<Class<? extends LogicalDocument>, BaseReportService<? extends LogicalDocument>> services = new HashMap<>();

    @Autowired
    public MainReportService(ReportDao reportDao, OccurrenceReportService occurrenceReportService,
                             AttachmentService attachmentService) {
        this.reportDao = reportDao;
        this.occurrenceReportService = occurrenceReportService;
        this.attachmentService = attachmentService;
    }

    @PostConstruct
    private void initServiceMap() {
        registerService(OccurrenceReport.class, occurrenceReportService);
    }

    private void registerService(Class<? extends LogicalDocument> cls,
                                 BaseReportService<? extends LogicalDocument> service) {
        entitiesToOwlClasses.put(EntityToOwlClassMapper.getOwlClassForEntity(cls), cls);
        services.put(cls, service);
    }

    @Override
    public List<ReportDto> findAll() {
        final List<LogicalDocument> reports = new ArrayList<>();
        services.values().forEach(service -> reports.addAll(service.findAll()));
        return reports.stream().map(LogicalDocument::toReportDto)
                      .sorted(new ReportLastModifiedComparator())
                      .collect(Collectors.toList());
    }

    @Override
    public Page<ReportDto> findAll(Pageable pageSpec, Collection<ReportFilter> filters) {
        final List<LogicalDocument> reports = new ArrayList<>();
        // Combine reports from all services
        long totalOfTotals = 0;
        for (BaseReportService<? extends LogicalDocument> service : services.values()) {
            final Page<? extends LogicalDocument> page = service.findAll(pageSpec, filters);
            reports.addAll(page.getContent());
            totalOfTotals += page.getTotalElements();
        }
        final List<ReportDto> result = reports.stream().map(LogicalDocument::toReportDto)
                                              .sorted(new ReportLastModifiedComparator())
                                              .collect(Collectors.toList());
        // And return corresponding page
        return new PageImpl<>(result.subList(0, Math.min(result.size(), pageSpec.getPageSize())), pageSpec,
                totalOfTotals);
    }

    @Override
    public <T extends LogicalDocument> void persist(T report) {
        Objects.requireNonNull(report);
        resolveService(report).persist(report);
    }

    private <T extends LogicalDocument> BaseReportService<T> resolveService(T instance) {
        if (!services.containsKey(instance.getClass())) {
            throw new UnsupportedReportTypeException("No service found for instance of class " + instance.getClass());
        }
        return (BaseReportService<T>) services.get(instance.getClass());
    }

    private <T extends LogicalDocument> BaseReportService<T> resolveService(Set<String> types) {
        for (String type : types) {
            if (entitiesToOwlClasses.containsKey(type)) {
                return (BaseReportService<T>) services.get(entitiesToOwlClasses.get(type));
            }
        }
        throw new UnsupportedReportTypeException("No service found for instance with types " + types);
    }

    @Override
    public <T extends LogicalDocument> T findByKey(String key) {
        Objects.requireNonNull(key);
        final Set<String> types = reportDao.getReportTypes(key);
        if (types.isEmpty()) {  // No types -> no instance
            return null;
        }
        final BaseReportService<T> service = resolveService(types);
        return service.findByKey(key);
    }

    @Override
    public <T extends LogicalDocument> void update(T report) {
        Objects.requireNonNull(report);
        resolveService(report).update(report);
    }

    @Override
    public <T extends LogicalDocument> T findLatestRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final Set<String> types = reportDao.getChainTypes(fileNumber);
        if (types.isEmpty()) {
            return null;
        }
        final BaseReportService<T> service = resolveService(types);
        return service.findLatestRevision(fileNumber);
    }

    @Override
    public void removeReportChain(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final Set<String> types = reportDao.getChainTypes(fileNumber);
        if (types.isEmpty()) {
            return;
        }
        resolveService(types).removeReportChain(fileNumber);
        attachmentService.deleteAttachments(fileNumber);
    }

    @Override
    public List<ReportRevisionInfo> getReportChainRevisions(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        return reportDao.getReportChainRevisions(fileNumber);
    }

    @Override
    public <T extends LogicalDocument> T createNewRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final Set<String> types = reportDao.getChainTypes(fileNumber);
        if (types.isEmpty()) {
            throw NotFoundException.create("Report chain", fileNumber);
        }
        final T previousRevision = findLatestRevision(fileNumber);
        final BaseReportService<T> service = resolveService(types);
        final T newRevision = service.createNewRevision(fileNumber);
        attachmentService.copyAttachments(previousRevision, newRevision);
        return newRevision;
    }

    @Override
    public <T extends LogicalDocument> T findRevision(Long fileNumber, Integer revision) {
        Objects.requireNonNull(fileNumber);
        Objects.requireNonNull(revision);
        final Set<String> types = reportDao.getChainTypes(fileNumber);
        if (types.isEmpty()) {
            return null;
        }
        final BaseReportService<T> service = resolveService(types);
        return service.findRevision(fileNumber, revision);
    }

    @Override
    public <T extends LogicalDocument> void transitionToNextPhase(T report) {
        Objects.requireNonNull(report);
        resolveService(report).transitionToNextPhase(report);
    }

    @Override
    public void addAttachment(AbstractReport report, String fileName, String description, InputStream content) {
        attachmentService.addAttachment(report, fileName, content);
        addResourceReference(report, fileName, description);
    }

    private void addResourceReference(AbstractReport report, String fileName, String description) {
        final Resource resource = new Resource();
        resource.setReference(fileName);
        resource.setDescription(description);
        resource.setTypes(Collections.singleton(Vocabulary.s_c_SensoryData));
        report.addReference(resource);
        resolveService(report).update(report);
    }
}
