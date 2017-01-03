package cz.cvut.kbss.reporting.data.audit.safa;

import cz.cvut.kbss.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.reporting.model.audit.AuditReport;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class AuditReportComparatorTest {

    private final AuditReportComparator comparator = new AuditReportComparator();

    @Test
    public void auditFindingHashHandlesMissingLastStatusModificationDate() {
        final AuditFinding finding = AuditReportGenerator.generateFinding();
        final int hash = comparator.auditFindingHash(finding);
        assertNotEquals(0, hash);
    }

    @Test
    public void equalsReturnsTrueForTwoMatchingAuditReports() {
        final AuditReport report = generateAuditReport(false);
        final AuditReport copy = copyAuditReport(report);
        assertTrue(comparator.equals(report, copy));
    }

    private AuditReport generateAuditReport(boolean withFindings) {
        final AuditReport report = AuditReportGenerator.generateAuditReport(true);
        report.setUri(Generator.generateUri());
        report.getAudit().setUri(Generator.generateUri());
        if (withFindings) {
            report.getAudit().setFindings(AuditReportGenerator.generateFindings());
        }
        return report;
    }

    private AuditReport copyAuditReport(AuditReport report) {
        final AuditReport copy = new AuditReport(report);
        copy.setUri(report.getUri());
        copy.getAudit().setUri(report.getAudit().getUri());
        return copy;
    }

    @Test
    public void equalsReturnsTrueForTwoMatchingAuditReportsWithFindings() {
        final AuditReport report = generateAuditReport(true);
        final AuditReport copy = copyAuditReport(report);
        assertTrue(comparator.equals(report, copy));
    }
}