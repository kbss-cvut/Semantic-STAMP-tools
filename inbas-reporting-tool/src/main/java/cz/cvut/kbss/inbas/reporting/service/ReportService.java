package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.exception.InvestigationExistsException;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.reporting.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.reporting.model.reports.Report;
import cz.cvut.kbss.inbas.reporting.util.Vocabulary;

import java.net.URI;
import java.util.List;

/**
 * Main facade to the report-related business logic.
 * <p>
 * This class should be used by the higher-level layers instead of the preliminary and investigation report-specific
 * services.
 */
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
     * Vocabulary#PreliminaryReport}.
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

    /**
     * Updates the specified report.
     *
     * @param report The report to update
     * @throws ValidationException If the report is not valid
     */
    void update(Report report);

    /**
     * Removes all reports in report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @throws NotFoundException If there is no report chain with the specified file number
     */
    void removeReportChain(Long fileNumber);

    /**
     * Creates new report revision in report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return The new revision
     */
    Report createNewRevision(Long fileNumber);

    /**
     * Gets report with latest revision in report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return Latest revision report or {@code null} if there is no matching report chain
     */
    Report findLatestRevision(Long fileNumber);

    /**
     * Gets report in report chain with the specified file number and with the specified revision number.
     *
     * @param fileNumber Report chain identifier
     * @param revision   Revision number
     * @return Matching report or {@code null}
     */
    Report findRevision(Long fileNumber, Integer revision);

    /**
     * Starts investigation from the latest preliminary report in a report chain identified by the specified file
     * number.
     *
     * @param fileNumber Report chain identifier
     * @return The newly created investigation
     * @throws InvestigationExistsException When the report chain is already in
     *                                                                         investigation phase
     */
    InvestigationReport startInvestigation(Long fileNumber);
}
