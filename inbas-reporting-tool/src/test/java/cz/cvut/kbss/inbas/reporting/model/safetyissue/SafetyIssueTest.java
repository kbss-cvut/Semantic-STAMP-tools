package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SafetyIssueTest {

    @Test
    public void copyOfCopiesNameAndTypes() {
        final SafetyIssue original = SafetyIssueReportGenerator.generateSafetyIssue();
        final SafetyIssue copy = SafetyIssue.copyOf(original);
        assertNotNull(copy);
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getTypes(), copy.getTypes());
    }

    @Test
    public void copyOfClonesFactorGraph() {
        final SafetyIssue original = SafetyIssueReportGenerator.generateSafetyIssueWithFactorGraph();
        final SafetyIssue copy = SafetyIssue.copyOf(original);
        verifyChildren(original.getChildren(), copy.getChildren());
    }

    private void verifyChildren(Set<Event> expected, Set<Event> actual) {
        if (expected == null) {
            assertNull(actual);
            return;
        }
        assertEquals(expected.size(), actual.size());
        boolean found;
        for (Event e : expected) {
            found = false;
            for (Event ee : actual) {
                if (e.getIndex().equals(ee.getIndex())) {
                    assertNotSame(e, ee);
                    found = true;
                    verifyChildren(e.getChildren(), ee.getChildren());
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void copyOfReusesReportsTheIssueIsBasedOn() {
        final SafetyIssue original = SafetyIssueReportGenerator.generateSafetyIssue();
        final Set<OccurrenceReport> reports = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(2, 5); i++) {
            reports.add(OccurrenceReportGenerator.generateOccurrenceReport(true));
        }
        original.setBasedOn(reports);

        final SafetyIssue copy = SafetyIssue.copyOf(original);
        assertEquals(original.getBasedOn(), copy.getBasedOn());
    }
}
