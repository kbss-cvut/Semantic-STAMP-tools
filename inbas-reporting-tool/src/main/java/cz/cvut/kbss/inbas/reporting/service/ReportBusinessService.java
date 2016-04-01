package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model_new.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model_new.Report;

import java.util.List;

/**
 * Main facade to the report-related business logic.
 * <p>
 * This class should be used by the higher-level layers instead of the report-type specific services.
 */
public interface ReportBusinessService {

    // NOTE: This interface currently uses LogicalDocument as type of most of the methods. This is because all reports
    // implement that interface. Once JOPA supports inheritance, LogicalDocument will be replaced with the base class
    // Report

    /**
     * Gets all reports.
     *
     * @return All reports in the system
     */
    List<Report> findAll();

    /**
     * Creates new report.
     *
     * @param report The instance to persist
     */
    <T extends LogicalDocument> void persist(T report);

    /**
     * Updates the specified report.
     *
     * @param report Updated report instance
     */
    <T extends LogicalDocument> void update(T report);

    /**
     * Finds instance with the specified key.
     *
     * @param key Instance key
     * @return Matching instance or {@code null}, if none exists
     */
    <T extends LogicalDocument> T findByKey(String key);

    /**
     * Gets report with latest revision in report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return Latest revision report or {@code null} if there is no matching report chain
     */
    <T extends LogicalDocument> T findLatestRevision(Long fileNumber);

    /**
     * Removes all reports in a report chain with the specified file number.
     * <p>
     * Does nothing if the report chain does not exist.
     *
     * @param fileNumber Report chain identifier
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
    <T extends LogicalDocument> T createNewRevision(Long fileNumber);

    /**
     * Gets report in report chain with the specified file number and with the specified revision number.
     *
     * @param fileNumber Report chain identifier
     * @param revision   Revision number
     * @return Matching report or {@code null}
     */
    <T extends LogicalDocument> T findRevision(Long fileNumber, Integer revision);
}
