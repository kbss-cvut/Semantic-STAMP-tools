package cz.cvut.kbss.inbas.reporting.model.audit;

import cz.cvut.kbss.inbas.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuditTest {

    @Test
    public void constructorAddsEventToTypes() {
        final Audit a = new Audit();
        assertTrue(a.getTypes().contains(Vocabulary.s_c_Event));
    }

    @Test
    public void copyConstructorCopiesRelevantAttributes() {
        final Audit original = AuditReportGenerator.generateAudit();
        original.setUri(Generator.generateUri());
        original.setLocation(Generator.generateUri());
        original.setAuditee(Generator.generateOrganization());

        final Audit copy = new Audit(original);
        assertNull(copy.getUri());
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getStartDate(), copy.getStartDate());
        assertEquals(original.getEndDate(), copy.getEndDate());
        assertEquals(original.getLocation(), copy.getLocation());
        assertEquals(original.getAuditee(), copy.getAuditee());
        assertEquals(original.getTypes(), copy.getTypes());
    }

    @Test
    public void copyConstructorCopiesAuditFindings() {
        final Audit original = AuditReportGenerator.generateAudit();
        original.setFindings(AuditReportGenerator.generateFindings());
        original.getFindings().forEach(f -> f.setUri(Generator.generateUri()));

        final Audit copy = new Audit(original);
        assertEquals(original.getFindings().size(), copy.getFindings().size());
        boolean found;
        for (AuditFinding f : original.getFindings()) {
            found = false;
            for (AuditFinding ff : copy.getFindings()) {
                if (f.getDescription().equals(ff.getDescription())) {
                    found = true;
                    assertNotSame(f, ff);
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void copyConstructorCopiesQuestionTree() {
        final Audit original = AuditReportGenerator.generateAudit();
        original.setQuestion(Generator.generateQuestions(2));

        final Audit copy = new Audit(original);
        assertNotNull(copy.getQuestion());
        assertNotSame(original.getQuestion(), copy.getQuestion());
    }
}
