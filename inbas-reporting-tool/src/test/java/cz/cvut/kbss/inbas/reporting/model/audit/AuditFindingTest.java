package cz.cvut.kbss.inbas.reporting.model.audit;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.util.TestUtils;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuditFindingTest {

    @Test
    public void constructorAddsEventToTypes() {
        final AuditFinding finding = new AuditFinding();
        assertTrue(finding.getTypes().contains(Vocabulary.s_c_Event));
    }

    @Test
    public void copyConstructorCopiesBasicAttributes() {
        final AuditFinding original = new AuditFinding();
        original.setDescription("I am the original audit finding.");
        original.getTypes().add(Generator.generateEventType().toString());
        original.setFactors(new HashSet<>());
        for (int i = 0; i < Generator.randomInt(1, 5); i++) {
            original.getFactors().add(Generator.generateEventType());
        }
        original.setLevel(1);

        final AuditFinding copy = new AuditFinding(original);
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getLevel(), copy.getLevel());
        assertEquals(original.getTypes(), copy.getTypes());
        assertEquals(original.getFactors(), copy.getFactors());
    }

    @Test
    public void copyConstructorCopiesCorrectiveMeasures() {
        final AuditFinding original = new AuditFinding();
        original.setCorrectiveMeasures(Generator.generateCorrectiveMeasureRequests());
        original.getCorrectiveMeasures().forEach(m -> m.setUri(Generator.generateUri()));
        original.setDescription("Test finding");
        original.setFactors(new HashSet<>());
        for (int i = 0; i < Generator.randomInt(1, 5); i++) {
            original.getFactors().add(Generator.generateEventType());
        }

        final AuditFinding copy = new AuditFinding(original);
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getFactors(), copy.getFactors());
        TestUtils.verifyCorrectiveMeasures(original.getCorrectiveMeasures(), copy.getCorrectiveMeasures());
    }
}
