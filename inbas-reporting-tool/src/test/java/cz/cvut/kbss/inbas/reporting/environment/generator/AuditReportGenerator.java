package cz.cvut.kbss.inbas.reporting.environment.generator;

import cz.cvut.kbss.inbas.reporting.model.audit.Audit;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;

import java.util.Date;

public class AuditReportGenerator {

    public static Audit generateAudit() {
        final Audit audit = new Audit();
        audit.setName("RandomAudit" + Generator.randomInt());
        audit.setStartDate(new Date(System.currentTimeMillis() - 1000 * 60 * 60));
        audit.setEndDate(new Date());
        return audit;
    }

    public static AuditReport generateAuditReport(boolean setAttributes) {
        final AuditReport report = new AuditReport();
        report.setAudit(generateAudit());
        if (setAttributes) {
            Generator.setReportAttributes(report);
        }
        return report;
    }
}
