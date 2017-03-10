package cz.cvut.kbss.reporting.model.safetyissue;

import cz.cvut.kbss.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.reporting.environment.util.FactorGraphVerifier;
import cz.cvut.kbss.reporting.model.Occurrence;
import cz.cvut.kbss.reporting.model.audit.AuditFinding;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SafetyIssueTest {

    @Test
    public void copyOfCopiesBasicAttributes() {
        final SafetyIssue original = SafetyIssueReportGenerator.generateSafetyIssue();
        final SafetyIssue copy = SafetyIssue.copyOf(original);
        assertNotNull(copy);
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getTypes(), copy.getTypes());
        assertEquals(original.getState(), copy.getState());
    }

    @Test
    public void copyOfClonesDescendantsInFactorGraph() {
        final SafetyIssue original = SafetyIssueReportGenerator.generateSafetyIssueWithDescendantEvents();
        final SafetyIssue copy = SafetyIssue.copyOf(original);
        new FactorGraphVerifier().verifyFactorGraph(original, copy);
    }

    @Test
    public void copyOfClonesFactorGraph() {
        final SafetyIssue original = SafetyIssueReportGenerator.generateSafetyIssueWithDescendantEvents();
        OccurrenceReportGenerator.generateFactors(original);
        final SafetyIssue copy = SafetyIssue.copyOf(original);
        new FactorGraphVerifier().verifyFactorGraph(original, copy);
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
