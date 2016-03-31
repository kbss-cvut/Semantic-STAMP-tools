package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;

public interface OccurrenceReportService extends BaseService<OccurrenceReport> {

    /**
     * Create new revision of report chain with the specified identifier.
     *
     * @param fileNumber Report chain identifier
     * @return The new report revision
     * @throws NotFoundException If there is no <b>occurrence report</b> chain with the specified identifier
     */
    OccurrenceReport createNewRevision(Long fileNumber);

    /**
     * Gets latest revision report in report chain with the specified identifier.
     *
     * @param fileNumber Report chain identifier
     * @return Report with latest revision, or {@code null}, if there is no <b>occurrence report</b> chain with the
     * specified identifier
     */
    OccurrenceReport findLatestRevision(Long fileNumber);

    /**
     * Gets report with the specified revision in report chain with the specified identifier.
     *
     * @param fileNumber Report chain identifier
     * @param revision   Revision number
     * @return Report with the specified revision number, or {@code null}, if there is no <b>occurrence report</b> chain
     * with the specified identifier or it contains no such revision
     */
    OccurrenceReport findRevision(Long fileNumber, Integer revision);
}
