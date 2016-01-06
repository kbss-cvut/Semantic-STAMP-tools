package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;

import java.util.List;

public interface PreliminaryReportService extends BaseService<PreliminaryReport> {

    PreliminaryReport findByKey(String key);

    /**
     * Creates and persists new revision of the specified report.
     *
     * @param report The report to create new revision for
     * @return New preliminary report
     */
    PreliminaryReport createNewRevision(PreliminaryReport report);

    /**
     * Gets a list of revision info objects for the specified occurrence.
     * <p>
     * The revision objects specify report revisions for the occurrence. They are ordered by the revision number in
     * descending order.
     *
     * @param occurrence Occurrence to get report revisions for
     * @return List of report revisions
     */
    List<ReportRevisionInfo> getRevisionsForOccurrence(Occurrence occurrence);
}
