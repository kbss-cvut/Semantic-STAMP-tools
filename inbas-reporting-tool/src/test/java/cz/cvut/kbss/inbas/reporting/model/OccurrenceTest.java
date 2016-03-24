package cz.cvut.kbss.inbas.reporting.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OccurrenceTest {

    private Occurrence occurrence = new Occurrence();

    @Test
    public void forwardPhaseTransitionIsSupported() {
        assertEquals(ReportingPhase.INITIAL, occurrence.getReportingPhase());
        occurrence.transitionToPhase(ReportingPhase.PRELIMINARY);
        assertEquals(ReportingPhase.PRELIMINARY, occurrence.getReportingPhase());
        occurrence.transitionToPhase(ReportingPhase.INVESTIGATION);
        assertEquals(ReportingPhase.INVESTIGATION, occurrence.getReportingPhase());
    }

    @Test
    public void tryingToTransitionFromPreliminaryToInitialDoesNothing() {
        occurrence.setReportingPhase(ReportingPhase.PRELIMINARY);
        occurrence.transitionToPhase(ReportingPhase.INITIAL);
        assertEquals(ReportingPhase.PRELIMINARY, occurrence.getReportingPhase());
    }

    @Test
    public void tryingToTransitionFromInvestigationToPreliminaryOrInitialDoesNothing() {
        occurrence.setReportingPhase(ReportingPhase.INVESTIGATION);
        occurrence.transitionToPhase(ReportingPhase.INITIAL);
        assertEquals(ReportingPhase.INVESTIGATION, occurrence.getReportingPhase());
        occurrence.transitionToPhase(ReportingPhase.PRELIMINARY);
        assertEquals(ReportingPhase.INVESTIGATION, occurrence.getReportingPhase());
    }
}