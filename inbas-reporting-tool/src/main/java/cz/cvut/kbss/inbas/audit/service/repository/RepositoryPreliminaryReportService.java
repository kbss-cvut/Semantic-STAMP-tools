package cz.cvut.kbss.inbas.audit.service.repository;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.CorrectiveMeasure;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.ValidatableReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.CorrectiveMeasureDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.EventTypeAssessmentDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PreliminaryReportDao;
import cz.cvut.kbss.inbas.audit.service.PreliminaryReportService;
import cz.cvut.kbss.inbas.audit.service.security.SecurityUtils;
import cz.cvut.kbss.inbas.audit.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RepositoryPreliminaryReportService extends BaseRepositoryService<PreliminaryReport>
        implements PreliminaryReportService {

    @Autowired
    private Validator<ValidatableReport> reportValidator;

    @Autowired
    private PreliminaryReportDao preliminaryReportDao;

    @Autowired
    private EventTypeAssessmentDao eventTypeAssessmentDao;

    @Autowired
    private CorrectiveMeasureDao correctiveMeasureDao;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    protected GenericDao<PreliminaryReport> getPrimaryDao() {
        return preliminaryReportDao;
    }

    public PreliminaryReport findByKey(String key) {
        return preliminaryReportDao.findByKey(key);
    }

    @Override
    public void persist(PreliminaryReport report) {
        report.setAuthor(securityUtils.getCurrentUser());
        report.setCreated(new Date());
        reportValidator.validate(report);
        report.getOccurrence().transitionToPhase(ReportingPhase.PRELIMINARY);
        preliminaryReportDao.persist(report);
    }

    @Override
    public void update(PreliminaryReport report) {
        if (report.getUri() == null || report.getKey() == null) {
            throw new IllegalArgumentException("Updated report missing URI or key. " + report);
        }
        report.setLastEdited(new Date());
        report.setLastEditedBy(securityUtils.getCurrentUser());
        reportValidator.validate(report);
        final PreliminaryReport original = preliminaryReportDao.find(report.getUri());
        assert original != null;
        deleteObsoleteStatements(report, original);
        preliminaryReportDao.update(report);
    }

    private void deleteObsoleteStatements(PreliminaryReport updated, PreliminaryReport original) {
        removeObsoleteEventTypeAssessments(updated, original);
        removeObsoleteCorrectiveMeasures(updated, original);
    }

    private void removeObsoleteEventTypeAssessments(PreliminaryReport updated, PreliminaryReport original) {
        if (original.getTypeAssessments() != null) {
            // We have to use ids, because the entities don't override equals/hashCode
            final Set<URI> updatedIds =
                    updated.getTypeAssessments() == null ? Collections.emptySet() : updated.getTypeAssessments()
                                                                                           .stream()
                                                                                           .map(EventTypeAssessment::getUri)
                                                                                           .collect(
                                                                                                   Collectors.toSet());
            final List<EventTypeAssessment> toRemove = original.getTypeAssessments().stream().filter(ass ->
                    !updatedIds.contains(ass.getUri())).collect(Collectors.toList());
            eventTypeAssessmentDao.remove(toRemove);
        }
    }

    private void removeObsoleteCorrectiveMeasures(PreliminaryReport updated, PreliminaryReport original) {
        if (original.getCorrectiveMeasures() != null) {
            // We have to use ids, because the entities don't override equals/hashCode
            final Set<URI> updatedIds =
                    updated.getCorrectiveMeasures() == null ? Collections.emptySet() : updated.getCorrectiveMeasures()
                                                                                              .stream().map(
                                    CorrectiveMeasure::getUri).collect(Collectors.toSet());
            final List<CorrectiveMeasure> toRemove = original.getCorrectiveMeasures().stream()
                                                             .filter(st -> !updatedIds.contains(st.getUri()))
                                                             .collect(Collectors.toList());
            correctiveMeasureDao.remove(toRemove);
        }
    }

    @Override
    public List<PreliminaryReport> findByOccurrence(Occurrence occurrence) {
        Objects.requireNonNull(occurrence);
        return preliminaryReportDao.findByOccurrence(occurrence);
    }
}
