package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.SafetyIssueReportGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SafetyIssueRiskAssessmentTest {

    @Test
    public void copyConstructorCopiesAllAttributes() {
        final SafetyIssueRiskAssessment original = SafetyIssueReportGenerator.generateSira();
        original.setUri(Generator.generateUri());
        original.setSira(Generator.generateUri());
        final SafetyIssueRiskAssessment copy = new SafetyIssueRiskAssessment(original);
        assertNull(copy.getUri());
        assertEquals(original.getInitialEventFrequency(), copy.getInitialEventFrequency());
        assertEquals(original.getBarrierUosAvoidanceFailFrequency(), copy.getBarrierUosAvoidanceFailFrequency());
        assertEquals(original.getBarrierRecoveryFailFrequency(), copy.getBarrierRecoveryFailFrequency());
        assertEquals(original.getAccidentSeverity(), copy.getAccidentSeverity());
        assertEquals(original.getSira(), copy.getSira());
    }
}
