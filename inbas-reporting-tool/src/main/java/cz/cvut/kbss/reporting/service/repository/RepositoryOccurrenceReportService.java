package cz.cvut.kbss.reporting.service.repository;

import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.model.Occurrence;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.util.factorgraph.traversal.FactorGraphTraverser;
import cz.cvut.kbss.reporting.model.util.factorgraph.traversal.IdentityBasedFactorGraphTraverser;
import cz.cvut.kbss.reporting.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.reporting.persistence.dao.OwlKeySupportingDao;
import cz.cvut.kbss.reporting.service.OccurrenceReportService;
import cz.cvut.kbss.reporting.service.arms.ArmsService;
import cz.cvut.kbss.reporting.service.data.eccairs.EccairsReportSynchronizationService;
import cz.cvut.kbss.reporting.service.options.ReportingPhaseService;
import cz.cvut.kbss.reporting.service.validation.OccurrenceReportValidator;
import cz.cvut.kbss.reporting.service.visitor.EventTypeSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Objects;

@Service
public class RepositoryOccurrenceReportService extends KeySupportingRepositoryService<OccurrenceReport>
        implements OccurrenceReportService {

    @Autowired
    private OccurrenceReportDao reportDao;

    @Autowired
    private ReportMetadataService reportMetadataService;

    @Autowired
    private OccurrenceReportValidator validator;

    @Autowired
    private ReportingPhaseService phaseService;

    @Autowired
    private ArmsService armsService;

    @Autowired
    private EventTypeSynchronizer eventTypeSynchronizer;

    @Autowired
    private EccairsReportSynchronizationService eccairsReportSynchronizationService;

    @Override
    protected OwlKeySupportingDao<OccurrenceReport> getPrimaryDao() {
        return reportDao;
    }

    @Override
    public OccurrenceReport findByKey(String key) {
        final OccurrenceReport r = super.findByKey(key);
        setArmsIndex(r);
        return r;
    }

    @Override
    protected void prePersist(OccurrenceReport instance) {
        synchronizeEventTypes(instance.getOccurrence());
        reportMetadataService.initMetadataForPersist(instance);
        if (instance.getPhase() == null) {
            instance.setPhase(phaseService.getDefaultPhase());
        }
        validator.validateForPersist(instance);
        ensureAircraftOperatorType(instance);
    }

    private void ensureAircraftOperatorType(OccurrenceReport instance) {
        if (instance.getOccurrence().getAircraft() != null) {
            instance.getOccurrence().getAircraft().addOperatorOrganizationType();
        }
    }

    @Override
    protected void preUpdate(OccurrenceReport instance) {
        reportMetadataService.initMetadataForUpdate(instance);
        synchronizeEventTypes(instance.getOccurrence());
        validator.validateForUpdate(instance, find(instance.getUri()));
        ensureAircraftOperatorType(instance);
    }

    private void synchronizeEventTypes(Occurrence occurrence) {
        final FactorGraphTraverser traverser = new IdentityBasedFactorGraphTraverser(eventTypeSynchronizer, null);
        traverser.traverse(occurrence);
    }

    @Override
    protected void postLoad(OccurrenceReport instance) {
        if (instance != null) {
            setArmsIndex(instance);
            instance.getAuthor().erasePassword();
            if (instance.getLastModifiedBy() != null) {
                instance.getLastModifiedBy().erasePassword();
            }
        }
    }

    @Override
    public OccurrenceReport createNewRevision(Long fileNumber) {
        final OccurrenceReport latest = findLatestRevision(fileNumber);
        if (latest == null) {
            throw NotFoundException.create("Occurrence report chain", fileNumber);
        }
        final OccurrenceReport newRevision = new OccurrenceReport(latest);
        newRevision.setRevision(latest.getRevision() + 1);
        reportMetadataService.initReportProvenanceMetadata(newRevision);
        reportDao.persist(newRevision);
        newRevision.setArmsIndex(armsService.calculateArmsIndex(newRevision));
        return newRevision;
    }

    @Override
    public OccurrenceReport findLatestRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final OccurrenceReport r = reportDao.findLatestRevision(fileNumber);
        setArmsIndex(r);
        return r;
    }

    private void setArmsIndex(OccurrenceReport report) {
        if (report != null) {
            report.setArmsIndex(armsService.calculateArmsIndex(report));
        }
    }

    @Override
    public OccurrenceReport findRevision(Long fileNumber, Integer revision) {
        Objects.requireNonNull(fileNumber);
        Objects.requireNonNull(revision);
        final OccurrenceReport r = reportDao.findRevision(fileNumber, revision);
        setArmsIndex(r);
        return r;
    }

    @Override
    public void transitionToNextPhase(OccurrenceReport report) {
        Objects.requireNonNull(report);
        report.setPhase(phaseService.nextPhase(report.getPhase()));
        update(report);
    }

    @Override
    public void removeReportChain(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        reportDao.removeReportChain(fileNumber);
    }

    @Override
    public OccurrenceReport createNewRevisionFromEccairs(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final OccurrenceReport latestRegular = findLatestRevision(fileNumber);
        if (latestRegular == null) {
            throw NotFoundException.create("Report chain", fileNumber);
        }
        String reportUri = eccairsReportSynchronizationService.getNewEccairsRevision(latestRegular);
        if (reportUri == null) {
            throw new NotFoundException("ECCAIRS report for report with key " + latestRegular.getKey() + " not found.");
        }
        final OccurrenceReport eccairsLatest = find(URI.create(reportUri));
        // TODO : remove the read only type from the eccairs reprots
        if (eccairsLatest == null) {
            throw new NotFoundException("ECCAIRS report for report with key " + latestRegular.getKey() + " not found.");
        }

        final OccurrenceReport newRevision = new OccurrenceReport(eccairsLatest);
        newRevision.setRevision(latestRegular.getRevision() + 1);
        newRevision.setFileNumber(fileNumber);
        reportMetadataService.initReportProvenanceMetadata(newRevision);
        reportDao.persist(newRevision);
        newRevision.setArmsIndex(armsService.calculateArmsIndex(newRevision));
        return newRevision;
    }
}
