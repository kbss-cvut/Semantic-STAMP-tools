package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;

public interface PreliminaryReportService extends BaseService<PreliminaryReport> {

    PreliminaryReport findByKey(String key);

    /**
     * Creates and persists new revision of the specified report.
     *
     * @param report The report to create new revision for
     * @return New preliminary report
     */
    PreliminaryReport createNewRevision(PreliminaryReport report);
}
