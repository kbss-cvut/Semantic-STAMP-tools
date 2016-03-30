package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model_new.Report;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceReportDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MainReportService implements ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(MainReportService.class);

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;

    @Override
    public List<OccurrenceReport> findAll() {
        return occurrenceReportDao.findAll();
    }

    @Override
    public Report find(URI uri) {
        return null;
    }

    @Override
    public Report findByKey(String key) {
        return null;
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
    public void update(Report report) {
        Objects.requireNonNull(report);

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
        LOG.debug("Deleted report chain under file number {}.", fileNumber);
    }

    @Override
    public Report createNewRevision(Long fileNumber) {
        final Report latestRevision = findLatestRevision(fileNumber);
        if (latestRevision == null) {
            throw NotFoundException.create("Report chain", fileNumber);
        }
        return null;
    }

    @Override
    public Report findLatestRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);

        return null;
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
}
