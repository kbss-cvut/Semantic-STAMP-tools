package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class ReportingPhaseTest {

    @Test
    public void initialReportTypeCorrespondsToInitialPhase() {
        assertEquals(ReportingPhase.INITIAL, ReportingPhase.fromType(URI.create(Vocabulary.InitialReport)));
    }

    @Test
    public void preliminaryReportTypeCorrespondsToPreliminaryPhase() {
        assertEquals(ReportingPhase.PRELIMINARY, ReportingPhase.fromType(URI.create(Vocabulary.PreliminaryReport)));
    }

    @Test
    public void investigationReportTypeCorrespondsToInvestigationPhase() {
        assertEquals(ReportingPhase.INVESTIGATION, ReportingPhase.fromType(URI.create(Vocabulary.InvestigationReport)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromTypeThrowsIllegalArgumentExceptionForUnsupportedType() {
        ReportingPhase.fromType(URI.create(Vocabulary.EventType));
    }
}