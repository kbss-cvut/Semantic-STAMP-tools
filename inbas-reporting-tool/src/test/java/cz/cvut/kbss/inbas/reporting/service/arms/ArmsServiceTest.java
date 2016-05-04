package cz.cvut.kbss.inbas.reporting.service.arms;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.arms.AccidentOutcome;
import cz.cvut.kbss.inbas.reporting.model.arms.BarrierEffectiveness;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ArmsServiceTest extends BaseServiceTestRunner {

    @Autowired
    private ArmsService armsService;

    private OccurrenceReport report;

    @Before
    public void setUp() {
        this.report = Generator.generateOccurrenceReport(false);
    }

    @Test
    public void testArmsCalculationForReport() {
        final List<ArmsTriple> testValues = initTestValues();
        for (ArmsTriple at : testValues) {
            report.setAccidentOutcome(at.accidentOutcome);
            report.setBarrierEffectiveness(at.barrierEffectiveness);
            final short armsIndex = armsService.calculateArmsIndex(report);
            assertEquals(at.expectedArmsIndex, armsIndex);
        }
    }

    @Test
    public void testArmsCalculation() {
        for (ArmsTriple at : initTestValues()) {
            final short armsIndex = armsService.calculateArmsIndex(at.accidentOutcome, at.barrierEffectiveness);
            assertEquals(at.expectedArmsIndex, armsIndex);
        }
    }

    @Test
    public void calculateArmsReturnsZeroForMissingBarrierEffectiveness() {
        report.setAccidentOutcome(AccidentOutcome.CATASTROPHIC);
        assertEquals(0, armsService.calculateArmsIndex(report));
    }

    @Test
    public void calculateArmsReturnsZeroForMissingAccidentOutcome() {
        report.setBarrierEffectiveness(BarrierEffectiveness.LIMITED);
        assertEquals(0, armsService.calculateArmsIndex(report));
    }

    @Test
    public void calculateArmsReturnZeroForMissingBothArmsValues() {
        assertEquals(0, armsService.calculateArmsIndex(report));
    }

    /**
     * ARMS values according to http://essi.easa.europa.eu/documents/ARMS.pdf, slide 27.
     */
    private List<ArmsTriple> initTestValues() {
        final List<ArmsTriple> lst = new ArrayList<>();
        lst.add(new ArmsTriple(AccidentOutcome.NEGLIGIBLE, BarrierEffectiveness.EFFECTIVE, (short) 1));
        lst.add(new ArmsTriple(AccidentOutcome.NEGLIGIBLE, BarrierEffectiveness.LIMITED, (short) 1));
        lst.add(new ArmsTriple(AccidentOutcome.NEGLIGIBLE, BarrierEffectiveness.MINIMAL, (short) 1));
        lst.add(new ArmsTriple(AccidentOutcome.NEGLIGIBLE, BarrierEffectiveness.NOT_EFFECTIVE, (short) 1));
        lst.add(new ArmsTriple(AccidentOutcome.MINOR, BarrierEffectiveness.EFFECTIVE, (short) 2));
        lst.add(new ArmsTriple(AccidentOutcome.MINOR, BarrierEffectiveness.LIMITED, (short) 4));
        lst.add(new ArmsTriple(AccidentOutcome.MINOR, BarrierEffectiveness.MINIMAL, (short) 20));
        lst.add(new ArmsTriple(AccidentOutcome.MINOR, BarrierEffectiveness.NOT_EFFECTIVE, (short) 100));
        lst.add(new ArmsTriple(AccidentOutcome.MAJOR, BarrierEffectiveness.EFFECTIVE, (short) 10));
        lst.add(new ArmsTriple(AccidentOutcome.MAJOR, BarrierEffectiveness.LIMITED, (short) 20));
        lst.add(new ArmsTriple(AccidentOutcome.MAJOR, BarrierEffectiveness.MINIMAL, (short) 100));
        lst.add(new ArmsTriple(AccidentOutcome.MAJOR, BarrierEffectiveness.NOT_EFFECTIVE, (short) 500));
        lst.add(new ArmsTriple(AccidentOutcome.CATASTROPHIC, BarrierEffectiveness.EFFECTIVE, (short) 50));
        lst.add(new ArmsTriple(AccidentOutcome.CATASTROPHIC, BarrierEffectiveness.LIMITED, (short) 100));
        lst.add(new ArmsTriple(AccidentOutcome.CATASTROPHIC, BarrierEffectiveness.MINIMAL, (short) 500));
        lst.add(new ArmsTriple(AccidentOutcome.CATASTROPHIC, BarrierEffectiveness.NOT_EFFECTIVE, (short) 2500));
        return lst;
    }

    private static final class ArmsTriple {
        private final AccidentOutcome accidentOutcome;
        private final BarrierEffectiveness barrierEffectiveness;
        private final short expectedArmsIndex;

        private ArmsTriple(AccidentOutcome accidentOutcome, BarrierEffectiveness barrierEffectiveness,
                           short expectedArmsIndex) {
            this.accidentOutcome = accidentOutcome;
            this.barrierEffectiveness = barrierEffectiveness;
            this.expectedArmsIndex = expectedArmsIndex;
        }
    }
}