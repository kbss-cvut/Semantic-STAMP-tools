package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;

import java.util.List;

public interface InvestigationReportService extends BaseService<InvestigationReport> {

    InvestigationReport findByKey(String key);

    /**
     * Creates and persists a new investigation report from the specified preliminary report.
     *
     * @param preliminaryReport Base for the new report
     * @return The created report
     */
    InvestigationReport createFromPreliminaryReport(PreliminaryReport preliminaryReport);

    /**
     * Creates new revision of the specified report.
     *
     * @param report The report to create new revision from
     * @return The new revision instance
     */
    InvestigationReport createNewRevision(InvestigationReport report);

    /**
     * Gets a list of report revisions concerning the specified occurrence.
     * <p>
     * The report revisions are ordered by revisions number in descending order.
     *
     * @param occurrence Occurrence to get revisions for
     * @return List of report revision info objects
     */
    List<ReportRevisionInfo> getRevisionsForOccurrence(Occurrence occurrence);
}
