package cz.cvut.kbss.inbas.audit.services.impl;

import cz.cvut.kbss.inbas.audit.model.reports.CorrectiveMeasure;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.*;
import cz.cvut.kbss.inbas.audit.services.OccurrenceReportService;
import cz.cvut.kbss.inbas.audit.services.validation.ReportValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ledvima1
 */
@Service
public class OccurrenceReportServiceImpl extends BaseService<OccurrenceReport> implements OccurrenceReportService {

    @Autowired
    private ReportValidator reportValidator;

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;

    @Autowired
    private EventTypeAssessmentDao eventTypeAssessmentDao;

    @Autowired
    private CorrectiveMeasureDao correctiveMeasureDao;

    @Autowired
    private SeverityAssessmentDao severityAssessmentDao;

    @Override
    protected GenericDao<OccurrenceReport> getPrimaryDao() {
        return occurrenceReportDao;
    }

    @Override
    public OccurrenceReport findByKey(String key) {
        return occurrenceReportDao.findByKey(key);
    }

    @Override
    public void persist(OccurrenceReport report) {
        reportValidator.validateReport(report);
        report.setCreated(new Date());
        report.setLastEdited(new Date());
        occurrenceReportDao.persist(report);
    }

    @Override
    public void update(OccurrenceReport report) {
        if (report.getUri() == null || report.getKey() == null) {
            throw new IllegalArgumentException("Updated report missing URI or key. " + report);
        }
        reportValidator.validateReport(report);
        report.setLastEdited(new Date());
        final OccurrenceReport original = occurrenceReportDao.find(report.getUri());
        assert original != null;
        deleteObsoleteStatements(report, original);
        occurrenceReportDao.update(report);
    }

    private void deleteObsoleteStatements(OccurrenceReport updated, OccurrenceReport original) {
        removeObsoleteEventTypeAssessments(updated, original);
        removeObsoleteCorrectiveMeasures(updated, original);
        removeObsoleteSeverityAssessment(updated, original);
    }

    private void removeObsoleteEventTypeAssessments(OccurrenceReport updated, OccurrenceReport original) {
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

    private void removeObsoleteCorrectiveMeasures(OccurrenceReport updated, OccurrenceReport original) {
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

    private void removeObsoleteSeverityAssessment(OccurrenceReport updated, OccurrenceReport original) {
        if (original.getSeverityAssessment() != null && updated.getSeverityAssessment() == null) {
            severityAssessmentDao.remove(original.getSeverityAssessment());
        }
    }
}
