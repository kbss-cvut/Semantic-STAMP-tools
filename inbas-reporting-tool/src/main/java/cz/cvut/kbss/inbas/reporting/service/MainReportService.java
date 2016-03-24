package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.exception.InvestigationExistsException;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model.ReportingPhase;
import cz.cvut.kbss.inbas.reporting.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.reporting.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.reporting.model.reports.Report;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceReportDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public void update(Report report) {
        Objects.requireNonNull(report);
        final Report original = findByKey(report.getKey());
        if (original == null) {
            throw NotFoundException.create("Original report", report.getKey());
        }
        verifyUpdateReportIdentity(original, report);
        if (report instanceof PreliminaryReport) {
            preliminaryReportService.update((PreliminaryReport) report);
        } else if (report instanceof InvestigationReport) {
            investigationService.update((InvestigationReport) report);
        } else {
            throw new IllegalArgumentException("Unsupported report type " + report.getClass());
        }
    }

    private void verifyUpdateReportIdentity(Report original, Report update) {
        if (!original.getUri().equals(update.getUri())) {
            throw new ValidationException(
                    "The updated report URI " + update.getUri() + " is different from the original URI " + original
                            .getUri() + "!");
        }
    }

    @Override
    public void removeReportChain(Long fileNumber) {
        final List<ReportRevisionInfo> reports = getReportChainRevisions(fileNumber);
        if (reports.isEmpty()) {
            throw NotFoundException.create("Report chain", fileNumber);
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

    @Override
    public Report createNewRevision(Long fileNumber) {
        final Report latestRevision = findLatestRevision(fileNumber);
        if (latestRevision == null) {
            throw NotFoundException.create("Report chain", fileNumber);
        }
        if (latestRevision instanceof PreliminaryReport) {
            return preliminaryReportService.createNewRevision((PreliminaryReport) latestRevision);
        } else {
            return investigationService.createNewRevision((InvestigationReport) latestRevision);
        }
    }

    @Override
    public Report findLatestRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);

        final InvestigationReport investigation = investigationService.findLatestRevision(fileNumber);
        if (investigation != null) {
            return investigation;
        }
        return preliminaryReportService.findLatestRevision(fileNumber);
    }

    @Override
    public Report findRevision(Long fileNumber, Integer revision) {
        Objects.requireNonNull(fileNumber);
        Objects.requireNonNull(revision);

        final List<ReportRevisionInfo> revisions = getReportChainRevisions(fileNumber);
        final Optional<ReportRevisionInfo> info = revisions.stream().filter(rev -> rev.getRevision().equals(revision))
                                                           .findFirst();
        if (!info.isPresent()) {
            return null;
        }
        return find(info.get().getUri());
    }

    @Override
    public InvestigationReport startInvestigation(Long fileNumber) {
        Objects.requireNonNull(fileNumber);

        final Report report = findLatestRevision(fileNumber);
        if (report == null) {
            throw NotFoundException.create("Report chain", fileNumber);
        }
        if (report.getPhase() == ReportingPhase.INVESTIGATION) {
            throw new InvestigationExistsException("Report chain " + fileNumber +
                    " is already in investigation phase. Cannot start new investigation.");
        }
        assert report instanceof PreliminaryReport;
        return investigationService.createFromPreliminaryReport((PreliminaryReport) report);
    }
}
