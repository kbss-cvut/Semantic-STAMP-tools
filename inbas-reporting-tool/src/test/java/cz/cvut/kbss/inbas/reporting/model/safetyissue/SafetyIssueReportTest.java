package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.SafetyIssueReportGenerator;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class SafetyIssueReportTest {

    @Test
    public void copyConstructorCopiesBasicFields() {
        final SafetyIssueReport original = SafetyIssueReportGenerator.generateSafetyIssueReport(true, false);
        original.setLastModifiedBy(Generator.getPerson());
        original.setLastModified(new Date());
        final SafetyIssueReport copy = new SafetyIssueReport(original);
        assertNull(copy.getUri());
        assertNull(copy.getKey());
        assertNull(copy.getAuthor());
        assertNull(copy.getDateCreated());
        assertNull(copy.getLastModified());
        assertNull(copy.getLastModifiedBy());
        assertEquals(original.getFileNumber(), copy.getFileNumber());
        assertEquals(original.getSummary(), copy.getSummary());
        assertEquals(original.getTypes(), copy.getTypes());
    }

    @Test
    public void copyConstructorCopiesCorrectiveMeasures() {
        final SafetyIssueReport original = SafetyIssueReportGenerator.generateSafetyIssueReport(true, true);
        assertFalse(original.getCorrectiveMeasures().isEmpty());
        final SafetyIssueReport copy = new SafetyIssueReport(original);
        assertEquals(original.getCorrectiveMeasures().size(), copy.getCorrectiveMeasures().size());
        boolean found;
        for (CorrectiveMeasure m : original.getCorrectiveMeasures()) {
            found = false;
            for (CorrectiveMeasure mm : copy.getCorrectiveMeasures()) {
                // We can't use URI for equality, because there is none in the copy, yet
                if (m.getDescription().equals(mm.getDescription())) {
                    found = true;
                    assertNotSame(m, mm);
                    assertEquals(m.getResponsiblePersons(), mm.getResponsiblePersons());
                    assertEquals(m.getResponsibleOrganizations(), mm.getResponsibleOrganizations());
                }
            }
            assertTrue(found);
        }
    }
}