package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.AuditReportDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OwlKeySupportingDao;
import cz.cvut.kbss.inbas.reporting.service.AuditReportService;
import cz.cvut.kbss.inbas.reporting.service.validation.AuditReportValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                        m.getResponsibleOrganizations().add(auditee);
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
        return null;
    }

    @Override
    public void removeReportChain(Long fileNumber) {

    }

    @Override
    public AuditReport createNewRevision(Long fileNumber) {
        return null;
    }

    @Override
    public AuditReport findRevision(Long fileNumber, Integer revision) {
        return null;
    }

    @Override
    public void transitionToNextPhase(AuditReport report) {

    }
}
