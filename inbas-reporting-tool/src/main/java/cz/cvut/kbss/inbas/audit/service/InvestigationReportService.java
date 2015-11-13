package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;

public interface InvestigationReportService extends BaseService<InvestigationReport> {

    InvestigationReport findByKey(String key);

    /**
     * Creates and persists a new investigation report from the specified preliminary report.
     *
     * @param preliminaryReport Base for the new report
     * @return The created report
     */
    InvestigationReport createFromPreliminaryReport(PreliminaryReport preliminaryReport);
}
