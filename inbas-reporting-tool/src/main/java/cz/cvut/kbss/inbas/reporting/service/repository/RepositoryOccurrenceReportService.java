package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OwlKeySupportingDao;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceReportService;
import cz.cvut.kbss.inbas.reporting.service.security.SecurityUtils;
import cz.cvut.kbss.inbas.reporting.service.validation.OccurrenceReportValidator;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Service
public class RepositoryOccurrenceReportService extends KeySupportingRepositoryService<OccurrenceReport>
        implements OccurrenceReportService {

    @Autowired
    private OccurrenceReportDao reportDao;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private OccurrenceReportValidator validator;

    @Override
    protected OwlKeySupportingDao<OccurrenceReport> getPrimaryDao() {
        return reportDao;
    }

    @Override
    public void persist(OccurrenceReport instance) {
        Objects.requireNonNull(instance);
        initReportData(instance);
        validator.validateForPersist(instance);
        super.persist(instance);
    }

    private void initReportData(OccurrenceReport instance) {
        instance.setAuthor(securityUtils.getCurrentUser());
        instance.setDateCreated(new Date());
        instance.setFileNumber(IdentificationUtils.generateFileNumber());
        instance.setRevision(Constants.INITIAL_REVISION);
    }

    @Override
    public void persist(Collection<OccurrenceReport> instances) {
        Objects.requireNonNull(instances);
        if (instances.isEmpty()) {
            return;
        }
        instances.forEach(r -> {
            initReportData(r);
            validator.validateForPersist(r);
        });
        super.persist(instances);
    }

    @Override
    public void update(OccurrenceReport instance) {
        Objects.requireNonNull(instance);
        instance.setLastModifiedBy(securityUtils.getCurrentUser());
        instance.setLastModified(new Date());
        validator.validateForUpdate(instance,find(instance.getUri()));
        super.update(instance);
    }

    @Override
    public OccurrenceReport createNewRevision(Long fileNumber) {
        final OccurrenceReport latest = findLatestRevision(fileNumber);
        if (latest == null) {
            throw NotFoundException.create("OccurrenceReport", fileNumber);
        }
        final OccurrenceReport newRevision = new OccurrenceReport(latest);
        newRevision.setRevision(latest.getRevision() + 1);
        newRevision.setAuthor(securityUtils.getCurrentUser());
        newRevision.setDateCreated(new Date());
        super.persist(newRevision);
        return newRevision;
    }

    @Override
    public OccurrenceReport findLatestRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        return reportDao.findLatestRevision(fileNumber);
    }

    @Override
    public OccurrenceReport findRevision(Long fileNumber, Integer revision) {
        Objects.requireNonNull(fileNumber);
        Objects.requireNonNull(revision);
        return reportDao.findRevision(fileNumber, revision);
    }

    @Override
    public void removeReportChain(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        reportDao.removeReportChain(fileNumber);
    }
}
