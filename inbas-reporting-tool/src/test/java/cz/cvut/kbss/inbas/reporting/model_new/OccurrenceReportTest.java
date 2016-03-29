package cz.cvut.kbss.inbas.reporting.model_new;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OccurrenceReportTest {

    @Test
    public void newInstanceContainsReportInTypes() {
        final OccurrenceReport report = new OccurrenceReport();
        assertTrue(report.getTypes().contains(Vocabulary.Report));
    }
}