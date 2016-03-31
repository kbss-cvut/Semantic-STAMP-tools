package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model_new.LogicalDocument;

import java.util.List;

/**
 * Main facade to the report-related business logic.
 * <p>
 * This class should be used by the higher-level layers instead of the report-type specific services.
 */
public interface ReportService<T extends LogicalDocument> {

    /**
     * Gets all reports.
     *
     * @return All reports in the system
     */
    List<T> findAll();

    /**
     * Creates new report.
     *
     * @param report The instance to persist
     */
    void persist(T report);

    /**
     * Finds report by its key.
     *
     * @param key Report key
     * @return Report or {@code null}
     */
    T findByKey(String key);

    /**
     * Updates the specified report.
     *
     * @param report Updated report instance
     */
    void update(T report);

    /**
     * Gets report with latest revision in report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return Latest revision report or {@code null} if there is no matching report chain
     */
    T findLatestRevision(Long fileNumber);

    /**
     * Removes all reports in a report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @throws NotFoundException If there is no report chain with the specified file number
     */
    void removeReportChain(Long fileNumber);

    /**
     * Gets all revisions in a report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return List of revisions, in descending order, or empty list if there is no such report chain
     */
    List<ReportRevisionInfo> getReportChainRevisions(Long fileNumber);

    /**
     * Creates new report revision in report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return The new revision
     * @throws NotFoundException If there is no report chain with the specified file number
     */
    T createNewRevision(Long fileNumber);

    /**
     * Gets report in report chain with the specified file number and with the specified revision number.
     *
     * @param fileNumber Report chain identifier
     * @param revision   Revision number
     * @return Matching report or {@code null}
     */
    T findRevision(Long fileNumber, Integer revision);
}
