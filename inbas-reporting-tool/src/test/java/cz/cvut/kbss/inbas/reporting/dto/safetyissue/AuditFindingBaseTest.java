package cz.cvut.kbss.inbas.reporting.dto.safetyissue;

import cz.cvut.kbss.inbas.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditFinding;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuditFindingBaseTest {

    @Test
    public void constructorFromAuditFindingCopiesBasicAttributes() {
        final AuditFinding finding = AuditReportGenerator.generateFinding();
        final AuditFindingBase base = new AuditFindingBase(finding);

        assertEquals(finding.getUri(), base.getUri());
        assertEquals(finding.getDescription(), base.getDescription());
        assertEquals(finding.getLevel(), base.getLevel());
    }

    @Test
    public void constructorFromAuditFindingAddsAuditFindingClassToTypes() {
        final AuditFinding finding = AuditReportGenerator.generateFinding();
        final AuditFindingBase base = new AuditFindingBase(finding);

        assertTrue(base.getTypes().containsAll(finding.getTypes()));
        assertTrue(base.getTypes().contains(Vocabulary.s_c_audit_finding));
    }
}
