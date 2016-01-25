package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.Report;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceReportDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainReportService implements ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(MainReportService.class);

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
        if (type == null) {
            return findAll();
        }
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
    public void removeReportChain(Long fileNumber) {
        final List<ReportRevisionInfo> reports = getReportChainRevisions(fileNumber);
        if (reports.isEmpty()) {
            return;
        }
        final List<PreliminaryReport> preliminaryToRemove = new ArrayList<>();
        final List<InvestigationReport> investigationToRemove = new ArrayList<>();
        for (ReportRevisionInfo revision : reports) {
            final Report r = find(revision.getUri());
            if (r.getPhase() == ReportingPhase.PRELIMINARY) {
                preliminaryToRemove.add((PreliminaryReport) r);
            } else if (r.getPhase() == ReportingPhase.INVESTIGATION) {
                investigationToRemove.add((InvestigationReport) r);
            } else {
                throw new IllegalStateException("Unexpected report type " + r.getPhase() + " of report " + r);
            }
        }
        preliminaryReportService.remove(preliminaryToRemove);
        investigationService.remove(investigationToRemove);
        LOG.debug("Deleted report chain under file number {}.", fileNumber);
    }
}
