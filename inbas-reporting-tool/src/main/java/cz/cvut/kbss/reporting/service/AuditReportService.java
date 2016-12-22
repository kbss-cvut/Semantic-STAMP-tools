package cz.cvut.kbss.reporting.service;

import cz.cvut.kbss.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.reporting.model.audit.AuditReport;

public interface AuditReportService extends BaseReportService<AuditReport> {

    /**
     * Finds audit report, which documents an audit which contains the specified finding.
     *
     * @param finding Finding contained in audit
     * @return Matching audit report or {@code null}, if there is no matching report
     */
    AuditReport findByAuditFinding(AuditFinding finding);
}
