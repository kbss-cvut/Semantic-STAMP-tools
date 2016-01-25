package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.Report;

import java.net.URI;
import java.util.List;

public interface ReportService {

    /**
     * Finds all occurrence reports.
     * <p>
     * Returns latest revisions for every report chain in the storage.
     *
     * @return Reports
     */
    List<OccurrenceReport> findAll();

    /**
     * Finds all occurrence reports of the specified type.
     * <p>
     * The type is expected to be a String representation of the type URI, e.g. {@link
     * cz.cvut.kbss.inbas.audit.util.Vocabulary#PreliminaryReport}.
     * <p>
     * This methods returns latest revisions of matching type for every report chain in the storage.
     *
     * @param type Report type. If {@code null}, the result is the same as {@link #findAll()}
     * @return List of reports
     */
    List<OccurrenceReport> findAll(String type);

    /**
     * Finds report by its URI.
     *
     * @param uri Report URI
     * @return Report or {@code null}
     */
    Report find(URI uri);

    /**
     * Finds report by its key.
     *
     * @param key Report key
     * @return Report or {@code null}
     */
    Report findByKey(String key);

    /**
     * Gets all revisions in a report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return List of revisions, in descending order
     */
    List<ReportRevisionInfo> getReportChainRevisions(Long fileNumber);

    void persist(PreliminaryReport report);

    void update(PreliminaryReport report);

    void update(InvestigationReport report);

    /**
     * Removes all reports in report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     */
    void removeReportChain(Long fileNumber);
}
