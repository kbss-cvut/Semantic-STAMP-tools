package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.Report;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
public class MainReportService implements ReportService {

    @Autowired
    private PreliminaryReportService preliminaryReportService;

    @Autowired
    private InvestigationReportService investigationService;

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;

    @Override
    public List<OccurrenceReport> findAll() {
        return occurrenceReportDao.findAll();
    }

    @Override
    public List<OccurrenceReport> findAll(String type) {
        return occurrenceReportDao.findAll(type);
    }

    @Override
    public Report find(URI uri) {
        final Report r = preliminaryReportService.find(uri);
        if (r != null) {
            return r;
        }
        return investigationService.find(uri);
    }

    @Override
    public Report findByKey(String key) {
        final Report r = preliminaryReportService.findByKey(key);
        if (r != null) {
            return r;
        }
        return investigationService.findByKey(key);
    }

    /**
     * Gets all revisions for a report chain identified by the file number.
     *
     * @param fileNumber Report chain identifier
     * @return List of revisions, in descending order
     */
    @Override
    public List<ReportRevisionInfo> getReportChainRevisions(Long fileNumber) {
        return occurrenceReportDao.getReportChainRevisions(fileNumber);
    }

    @Override
    public void persist(PreliminaryReport report) {
        preliminaryReportService.persist(report);
    }

    @Override
    public void update(PreliminaryReport report) {
        preliminaryReportService.update(report);
    }

    @Override
    public void update(InvestigationReport report) {
        investigationService.update(report);
    }

    @Override
    public <T extends Report> void remove(T report) {
        Objects.requireNonNull(report);
        if (report.getPhase() == ReportingPhase.PRELIMINARY) {
            preliminaryReportService.remove((PreliminaryReport) report);
        } else if (report.getPhase() == ReportingPhase.INVESTIGATION) {
            investigationService.remove((InvestigationReport) report);
        } else {
            throw new IllegalArgumentException("Invalid report phase " + report.getPhase());
        }
    }
}
