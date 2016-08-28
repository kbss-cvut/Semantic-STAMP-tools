package cz.cvut.kbss.inbas.reporting.environment.generator;

import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.audit.Audit;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;

import java.util.*;

public class AuditReportGenerator {

    public static Audit generateAudit() {
        final Audit audit = new Audit();
        audit.setName("RandomAudit" + Generator.randomInt());
        audit.setStartDate(new Date(System.currentTimeMillis() - 1000 * 60 * 60));
        audit.setEndDate(new Date());
        audit.setAuditee(Generator.generateOrganization());
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

    public static List<AuditReport> generateAuditReportChain(Person author) {
        final List<AuditReport> result = new ArrayList<>();
        AuditReport current = generateAuditReport(false);
        current.setRevision(Constants.INITIAL_REVISION);
        final Long fileNumber = IdentificationUtils.generateFileNumber();
        result.add(current);
        for (int i = 0; i < Generator.randomInt(1, 5); i++) {
            current.setKey(IdentificationUtils.generateKey());
            current.setFileNumber(fileNumber);
            current.setAuthor(author);
            final AuditReport newRevision = new AuditReport(current);
            newRevision.setRevision(current.getRevision() + 1);
            newRevision.setAuthor(current.getAuthor());
            newRevision.setDateCreated(new Date());
            result.add(newRevision);
            current = newRevision;
        }
        return result;
    }

    public static Set<AuditFinding> generateFindings() {
        final Set<AuditFinding> findings = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(3, 10); i++) {
            final AuditFinding finding = new AuditFinding();
            finding.setDescription("Random audit finding number " + i);
            finding.getTypes().add(Generator.generateEventType().toString());
            findings.add(finding);
        }
        return findings;
    }
}
