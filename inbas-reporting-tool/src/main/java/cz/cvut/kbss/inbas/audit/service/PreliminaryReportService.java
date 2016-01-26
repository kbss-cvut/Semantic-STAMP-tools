package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;

public interface PreliminaryReportService extends BaseService<PreliminaryReport> {

    PreliminaryReport findByKey(String key);

    /**
     * Finds latest preliminary report in report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return Latest revision of preliminary report in a chain or {@code null} if there is no matching chain
     */
    PreliminaryReport findLatestRevision(Long fileNumber);

    /**
     * Creates and persists new revision of the specified report.
     *
     * @param report The report to create new revision for
     * @return New preliminary report
     */
    PreliminaryReport createNewRevision(PreliminaryReport report);
}
