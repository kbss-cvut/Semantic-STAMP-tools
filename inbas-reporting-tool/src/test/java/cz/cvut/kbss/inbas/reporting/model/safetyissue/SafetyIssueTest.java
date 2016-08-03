package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.environment.generator.SafetyIssueReportGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SafetyIssueTest {

    @Test
    public void copyOfCopiesNameAndTypes() {
        final SafetyIssue original = SafetyIssueReportGenerator.generateSafetyIssue();
        final SafetyIssue copy = SafetyIssue.copyOf(original);
        assertNotNull(copy);
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getTypes(), copy.getTypes());
    }
}
