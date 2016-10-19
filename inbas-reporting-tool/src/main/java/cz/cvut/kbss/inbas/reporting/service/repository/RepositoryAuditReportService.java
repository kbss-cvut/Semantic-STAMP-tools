package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.AuditReportDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OwlKeySupportingDao;
import cz.cvut.kbss.inbas.reporting.service.AuditReportService;
import cz.cvut.kbss.inbas.reporting.service.validation.AuditReportValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
public class RepositoryAuditReportService extends KeySupportingRepositoryService<AuditReport>
        implements AuditReportService {

    @Autowired
    private AuditReportDao reportDao;

    @Autowired
    private ReportMetadataService reportMetadataService;

    @Autowired
    private AuditReportValidator validator;

    @Override
    protected OwlKeySupportingDao<AuditReport> getPrimaryDao() {
        return reportDao;
    }

    @Override
    protected void prePersist(AuditReport instance) {
        reportMetadataService.initMetadataForPersist(instance);
        validator.validateForPersist(instance);
        setResponsibleOrganizationOnMeasures(instance);
    }

    private void setResponsibleOrganizationOnMeasures(AuditReport instance) {
        final Organization auditee = instance.getAudit().getAuditee();
        if (instance.getAudit().getFindings() != null) {
            instance.getAudit().getFindings().stream().filter(f -> f.getCorrectiveMeasures() != null)
                    .forEach(f -> f.getCorrectiveMeasures().forEach(m -> {
                        m.setResponsibleOrganizations(Collections.singleton(auditee));
                    }));
        }
    }

    @Override
    protected void preUpdate(AuditReport instance) {
        final AuditReport original = reportDao.find(instance.getUri());
        if (original == null) {
            throw NotFoundException.create("Audit report", instance.getUri());
        }
        reportMetadataService.initMetadataForUpdate(instance);
        validator.validateForUpdate(instance, original);
        setResponsibleOrganizationOnMeasures(instance);
    }

    @Override
    public AuditReport findLatestRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        return reportDao.findLatestRevision(fileNumber);
    }

    @Override
    public void removeReportChain(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        reportDao.removeReportChain(fileNumber);
    }

    @Override
    public AuditReport createNewRevision(Long fileNumber) {
        final AuditReport latest = findLatestRevision(fileNumber);
        if (latest == null) {
            throw NotFoundException.create("Audit report chain", fileNumber);
        }
        final AuditReport newRevision = new AuditReport(latest);
        reportMetadataService.initReportProvenanceMetadata(newRevision);
        newRevision.setRevision(latest.getRevision() + 1);
        reportDao.persist(newRevision);
        return newRevision;
    }

    @Override
    public AuditReport findRevision(Long fileNumber, Integer revision) {
        Objects.requireNonNull(fileNumber);
        Objects.requireNonNull(revision);
        return reportDao.findRevision(fileNumber, revision);
    }

    @Override
    public void transitionToNextPhase(AuditReport report) {
        // Do nothing, no phases for safety issue reports
    }

    @Override
    public AuditReport findByAuditFinding(AuditFinding finding) {
        Objects.requireNonNull(finding);
        return reportDao.findByAuditFinding(finding);
    }
}
