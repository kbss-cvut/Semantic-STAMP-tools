package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.reporting.model.reports.PreliminaryReport;

public interface InvestigationReportService extends BaseService<InvestigationReport> {

    InvestigationReport findByKey(String key);

    /**
     * Finds latest investigation report in report chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return Latest revision of investigation report in a chain or {@code null} if there is no matching chain or it
     * has no investigation, yet
     */
    InvestigationReport findLatestRevision(Long fileNumber);

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
}
