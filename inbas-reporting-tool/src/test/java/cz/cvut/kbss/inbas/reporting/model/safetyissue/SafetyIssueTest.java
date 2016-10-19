package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditFinding;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SafetyIssueTest {

    @Test
    public void copyOfCopiesBasicAttributes() {
        final SafetyIssue original = SafetyIssueReportGenerator.generateSafetyIssue();
        final SafetyIssue copy = SafetyIssue.copyOf(original);
        assertNotNull(copy);
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getTypes(), copy.getTypes());
        assertEquals(original.getState(), copy.getState());
        assertEquals(original.getSira(), copy.getSira());
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
    public void copyOfReusesOccurrencesTheIssueIsBasedOn() {
        final SafetyIssue original = SafetyIssueReportGenerator.generateSafetyIssue();
        final Set<Occurrence> occurrences = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(2, 5); i++) {
            occurrences.add(OccurrenceReportGenerator.generateOccurrenceReport(true).getOccurrence());
        }
        original.setBasedOnOccurrences(occurrences);

        final SafetyIssue copy = SafetyIssue.copyOf(original);
        assertEquals(original.getBasedOnOccurrences(), copy.getBasedOnOccurrences());
    }

    @Test
    public void copyOfReusesAuditFindingsTheIssueIsBasedOn() {
        final SafetyIssue original = SafetyIssueReportGenerator.generateSafetyIssue();
        final Set<AuditFinding> findings = AuditReportGenerator.generateFindings();
        original.setBasedOnFindings(findings);

        final SafetyIssue copy = SafetyIssue.copyOf(original);
        assertEquals(original.getBasedOnFindings(), copy.getBasedOnFindings());
    }
}
