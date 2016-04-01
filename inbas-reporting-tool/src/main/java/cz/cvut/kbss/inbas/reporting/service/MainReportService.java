package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.exception.UnsupportedReportTypeException;
import cz.cvut.kbss.inbas.reporting.model_new.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model_new.Report;
import cz.cvut.kbss.inbas.reporting.model_new.util.EntityToOwlClassMapper;
import cz.cvut.kbss.inbas.reporting.persistence.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class MainReportService implements ReportBusinessService {

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private OccurrenceReportService occurrenceReportService;

    private final Map<String, Class<? extends LogicalDocument>> entitiesToOwlClasses = new HashMap<>();

    private final Map<Class<? extends LogicalDocument>, BaseReportService<? extends LogicalDocument>> services = new HashMap<>();

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
    public List<Report> findAll() {
        return reportDao.findAll();
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
        return service.createNewRevision(fileNumber);
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
}
