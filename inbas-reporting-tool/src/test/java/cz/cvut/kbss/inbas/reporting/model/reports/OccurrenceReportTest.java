package cz.cvut.kbss.inbas.reporting.model.reports;

import cz.cvut.kbss.inbas.reporting.model.ReportingPhase;
import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class OccurrenceReportTest {

    @Test
    public void getPhaseReturnsPreliminaryForPreliminaryReport() {
        final OccurrenceReport report = new OccurrenceReport();
        report.setTypes(new HashSet<>(Arrays.asList(Vocabulary.PreliminaryReport, Vocabulary.Report)));

        assertEquals(ReportingPhase.PRELIMINARY, report.getPhase());
    }

    @Test
    public void getPhaseReturnsInvestigationForInvestigationReport() {
        final OccurrenceReport report = new OccurrenceReport();
        report.setTypes(new HashSet<>(Arrays.asList(Vocabulary.InvestigationReport, Vocabulary.Report)));

        assertEquals(ReportingPhase.INVESTIGATION, report.getPhase());
    }

    @Test(expected = IllegalStateException.class)
    public void getPhaseThrowsIllegalStateForUnknownReportType() {
        final OccurrenceReport report = new OccurrenceReport();
        report.setTypes(new HashSet<>(Collections.singletonList(Vocabulary.Report)));

        report.getPhase();
    }
}