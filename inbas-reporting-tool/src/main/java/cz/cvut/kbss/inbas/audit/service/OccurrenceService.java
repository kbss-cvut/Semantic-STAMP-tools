package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.Report;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.InvestigationReportDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PreliminaryReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class OccurrenceService extends BaseService<Occurrence> {

    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private PreliminaryReportDao preliminaryReportDao;
    @Autowired
    private InvestigationReportDao investigationReportDao;

    @Override
    protected GenericDao<Occurrence> getPrimaryDao() {
        return occurrenceDao;
    }

    public Occurrence findByKey(String key) {
        return occurrenceDao.findByKey(key);
    }

    public Collection<Report> getReports(Occurrence occurrence) {
        final List<PreliminaryReport> preliminaryReports = preliminaryReportDao.findByOccurrence(occurrence);
        final List<InvestigationReport> investigationReports = investigationReportDao.findByOccurrence(occurrence);
        final List<Report> result = new ArrayList<>(preliminaryReports);
        result.addAll(investigationReports);
        return result;
    }

    public Collection<Report> getReports(Occurrence occurrence, ReportingPhase phase) {
        Objects.requireNonNull(occurrence);
        Objects.requireNonNull(phase);

        final List<? extends Report> result;
        switch (phase) {
            case PRELIMINARY:
                result = preliminaryReportDao.findByOccurrence(occurrence);
                break;
            case INVESTIGATION:
                result = investigationReportDao.findByOccurrence(occurrence);
                break;
            default:
                throw new IllegalArgumentException("Unsupported report phase " + phase);
        }
        return new ArrayList<>(result);
    }
}
