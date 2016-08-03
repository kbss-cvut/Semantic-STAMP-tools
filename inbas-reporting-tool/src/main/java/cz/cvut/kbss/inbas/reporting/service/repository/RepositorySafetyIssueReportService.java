package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OwlKeySupportingDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.SafetyIssueReportDao;
import cz.cvut.kbss.inbas.reporting.service.SafetyIssueReportService;
import cz.cvut.kbss.inbas.reporting.service.security.SecurityUtils;
import cz.cvut.kbss.inbas.reporting.service.validation.ReportValidator;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Objects;

@Repository
public class RepositorySafetyIssueReportService extends KeySupportingRepositoryService<SafetyIssueReport>
        implements SafetyIssueReportService {

    @Autowired
    private SafetyIssueReportDao dao;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ReportValidator validator;

    @Override
    protected OwlKeySupportingDao<SafetyIssueReport> getPrimaryDao() {
        return dao;
    }

    @Override
    protected void prePersist(SafetyIssueReport instance) {
        initReportData(instance);
        validator.validateForPersist(instance);
    }

    private void initReportData(SafetyIssueReport instance) {
        initReportProvenance(instance);
        instance.setFileNumber(IdentificationUtils.generateFileNumber());
        instance.setRevision(Constants.INITIAL_REVISION);
    }

    private void initReportProvenance(SafetyIssueReport instance) {
        instance.setAuthor(securityUtils.getCurrentUser());
        instance.setDateCreated(new Date());
    }

    @Override
    protected void preUpdate(SafetyIssueReport instance) {
        instance.setLastModifiedBy(securityUtils.getCurrentUser());
        instance.setLastModified(new Date());
    }

    @Override
    public SafetyIssueReport findLatestRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        return dao.findLatestRevision(fileNumber);
    }

    @Override
    public void removeReportChain(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        dao.removeReportChain(fileNumber);
    }

    @Override
    public SafetyIssueReport createNewRevision(Long fileNumber) {
        final SafetyIssueReport latest = findLatestRevision(fileNumber);
        if (latest == null) {
            throw NotFoundException.create(SafetyIssueReport.class.getSimpleName(), fileNumber);
        }
        final SafetyIssueReport newRevision = new SafetyIssueReport(latest);
        newRevision.setRevision(latest.getRevision() + 1);
        initReportProvenance(newRevision);
        dao.persist(newRevision);
        return newRevision;
    }

    @Override
    public SafetyIssueReport findRevision(Long fileNumber, Integer revision) {
        Objects.requireNonNull(fileNumber);
        Objects.requireNonNull(revision);
        return dao.findRevision(fileNumber, revision);
    }

    @Override
    public void transitionToNextPhase(SafetyIssueReport report) {
        // Do nothing (no phases are defined for safety issues)
    }
}
