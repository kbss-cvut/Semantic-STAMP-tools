package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.exception.ReportImportingException;
import cz.cvut.kbss.inbas.reporting.exception.UnsupportedReportTypeException;
import cz.cvut.kbss.inbas.reporting.model.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.inbas.reporting.model.util.DocumentDateAndRevisionComparator;
import cz.cvut.kbss.inbas.reporting.model.util.EntityToOwlClassMapper;
import cz.cvut.kbss.inbas.reporting.persistence.dao.ReportDao;
import cz.cvut.kbss.inbas.reporting.service.cache.ReportCache;
import cz.cvut.kbss.inbas.reporting.service.data.mail.ReportImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MainReportService implements ReportBusinessService {

    private static final Logger LOG = LoggerFactory.getLogger(MainReportService.class);

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private ReportCache reportCache;

    @Autowired
    private ReportImporter reportImporter;

    @Autowired
    private OccurrenceReportService occurrenceReportService;

    @Autowired
    private SafetyIssueReportService safetyIssueReportService;

    private final Map<String, Class<? extends LogicalDocument>> entitiesToOwlClasses = new HashMap<>();

    private final Map<Class<? extends LogicalDocument>, BaseReportService<? extends LogicalDocument>> services = new HashMap<>();

    @PostConstruct
    private void initServiceMap() {
        registerService(OccurrenceReport.class, occurrenceReportService);
        registerService(SafetyIssueReport.class, safetyIssueReportService);
    }

    private void registerService(Class<? extends LogicalDocument> cls,
                                 BaseReportService<? extends LogicalDocument> service) {
        entitiesToOwlClasses.put(EntityToOwlClassMapper.getOwlClassForEntity(cls), cls);
        services.put(cls, service);
    }

    @Override
    public List<ReportDto> findAll() {
        if (!reportCache.isEmpty()) {
            return reportCache.getAll();
        }
        final List<LogicalDocument> reports = new ArrayList<>();
        services.values().forEach(service -> reports.addAll(service.findAll()));
        final List<ReportDto> result = reports.stream().map(LogicalDocument::toReportDto).collect(Collectors.toList());
        result.forEach(reportCache::put);
        Collections.sort(result, new DocumentDateAndRevisionComparator());
        return result;
    }

    @Override
    public <T extends LogicalDocument> void persist(T report) {
        Objects.requireNonNull(report);
        resolveService(report).persist(report);
        reportCache.put(report.toReportDto());
    }

    private <T extends LogicalDocument> BaseReportService<T> resolveService(T instance) {
        if (!services.containsKey(instance.getClass())) {
            throw new UnsupportedReportTypeException("No service found for instance of class " + instance.getClass());
        }
        return (BaseReportService<T>) services.get(instance.getClass());
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

    private <T extends LogicalDocument> BaseReportService<T> resolveService(Set<String> types) {
        for (String type : types) {
            if (entitiesToOwlClasses.containsKey(type)) {
                return (BaseReportService<T>) services.get(entitiesToOwlClasses.get(type));
            }
        }
        throw new UnsupportedReportTypeException("No service found for instance with types " + types);
    }

    @Override
    public <T extends LogicalDocument> void update(T report) {
        Objects.requireNonNull(report);
        resolveService(report).update(report);
        reportCache.put(report.toReportDto());
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
        reportCache.evict(fileNumber);
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
        final BaseReportService<T> service = resolveService(types);
        final T newRevision = service.createNewRevision(fileNumber);
        reportCache.put(newRevision.toReportDto());
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
        reportCache.put(report.toReportDto());
    }

    @Override
    public <T extends LogicalDocument> T importReportFromFile(String fileName, InputStream input) {
        try {
            final List<URI> uris = reportImporter.process(new NamedStream(fileName, input));
            assert uris.size() > 0;
            if (uris.size() > 1) {
                LOG.warn("Multiple reports imported from file {}. Returning the first one.", fileName);
            }
            final URI reportUri = uris.get(0);
            final Set<String> types = reportDao.getReportTypes(reportUri);
            final BaseReportService<T> service = resolveService(types);
            return service.find(reportUri);
        } catch (Exception e) {
            LOG.error("Unable to import report from file {}.", fileName, e);
            throw new ReportImportingException("Unable to import report from file " + fileName, e);
        }
    }
}
