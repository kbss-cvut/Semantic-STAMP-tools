package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;

public class AuditReportDao extends BaseReportDao<AuditReport> {

    public AuditReportDao() {
        super(AuditReport.class);
    }


}
