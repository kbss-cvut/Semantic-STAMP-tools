package cz.cvut.kbss.inbas.reporting.service.arms;

import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.options.OptionsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

public class ArmsServiceTest {

    @Mock
    private OptionsService optionsService;

    @InjectMocks
    private ArmsServiceImpl armsService;

    private OccurrenceReport report;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.report = Generator.generateOccurrenceReport(false);
        doReturn(new RawJson(Environment.loadData("data/accidentOutcome.json", String.class))).when(optionsService)
                                                                                              .getOptions(
                                                                                                      "accidentOutcome");
        doReturn(new RawJson(Environment.loadData("data/barrierEffectiveness.json", String.class))).when(optionsService)
                                                                                                   .getOptions(
                                                                                                           "barrierEffectiveness");
    }

    @Test
    public void testArmsCalculationForReport() {
        armsService.initArmsAttributes();
        final List<ArmsTriple> testValues = initTestValues();
        for (ArmsTriple at : testValues) {
            report.setAccidentOutcome(at.accidentOutcome);
            report.setBarrierEffectiveness(at.barrierEffectiveness);
            final int armsIndex = armsService.calculateArmsIndex(report);
            assertEquals(at.expectedArmsIndex, armsIndex);
        }
    }

    @Test
    public void testArmsCalculation() {
        armsService.initArmsAttributes();
        for (ArmsTriple at : initTestValues()) {
            final int armsIndex = armsService.calculateArmsIndex(at.accidentOutcome, at.barrierEffectiveness);
            assertEquals(at.expectedArmsIndex, armsIndex);
        }
    }

    @Test
    public void calculateArmsReturnsZeroForMissingBarrierEffectiveness() {
        armsService.initArmsAttributes();
        report.setAccidentOutcome(Generator.ACCIDENT_CATASTROPHIC);
        assertEquals(0, armsService.calculateArmsIndex(report));
    }

    @Test
    public void calculateArmsReturnsZeroForMissingAccidentOutcome() {
        armsService.initArmsAttributes();
        report.setBarrierEffectiveness(Generator.BARRIER_LIMITED);
        assertEquals(0, armsService.calculateArmsIndex(report));
    }

    @Test
    public void calculateArmsReturnZeroForMissingBothArmsValues() {
        armsService.initArmsAttributes();
        assertEquals(0, armsService.calculateArmsIndex(report));
    }

    @Test
    public void calculateArmsReturnsZeroForUnknownAccidentOutcome() {
        armsService.initArmsAttributes();
        assertEquals(0, armsService
                .calculateArmsIndex(URI.create("http://unknownAccidentOutcome"), Generator.BARRIER_EFFECTIVE));
    }

    @Test
    public void calculateArmsReturnsZeroForUnknownBarrierEffectiveness() {
        armsService.initArmsAttributes();
        assertEquals(0, armsService
                .calculateArmsIndex(Generator.ACCIDENT_MINOR, URI.create("http://unknownBarrierEffectiveness")));
    }

    @Test
    public void calculateArmsReturnsZeroWhenItWasUnableToLoadArmsValues() {
        doReturn(new RawJson("")).when(optionsService).getOptions("accidentOutcome");
        armsService.initArmsAttributes();
        assertEquals(0, armsService.calculateArmsIndex(Generator.ACCIDENT_MINOR, Generator.BARRIER_EFFECTIVE));
    }

    /**
     * ARMS values according to https://essi.easa.europa.eu/documents/Methodology.pdf, page 19.
     */
    private List<ArmsTriple> initTestValues() {
        final List<ArmsTriple> lst = new ArrayList<>();
        lst.add(new ArmsTriple(Generator.ACCIDENT_NEGLIGIBLE, Generator.BARRIER_EFFECTIVE, 1));
        lst.add(new ArmsTriple(Generator.ACCIDENT_NEGLIGIBLE, Generator.BARRIER_LIMITED, 1));
        lst.add(new ArmsTriple(Generator.ACCIDENT_NEGLIGIBLE, Generator.BARRIER_MINIMAL, 1));
        lst.add(new ArmsTriple(Generator.ACCIDENT_NEGLIGIBLE, Generator.BARRIER_NOT_EFFECTIVE, 1));
        lst.add(new ArmsTriple(Generator.ACCIDENT_MINOR, Generator.BARRIER_EFFECTIVE, 2));
        lst.add(new ArmsTriple(Generator.ACCIDENT_MINOR, Generator.BARRIER_LIMITED, (short) 4));
        lst.add(new ArmsTriple(Generator.ACCIDENT_MINOR, Generator.BARRIER_MINIMAL, 20));
        lst.add(new ArmsTriple(Generator.ACCIDENT_MINOR, Generator.BARRIER_NOT_EFFECTIVE, 100));
        lst.add(new ArmsTriple(Generator.ACCIDENT_MAJOR, Generator.BARRIER_EFFECTIVE, 10));
        lst.add(new ArmsTriple(Generator.ACCIDENT_MAJOR, Generator.BARRIER_LIMITED, 21));
        lst.add(new ArmsTriple(Generator.ACCIDENT_MAJOR, Generator.BARRIER_MINIMAL, 101));
        lst.add(new ArmsTriple(Generator.ACCIDENT_MAJOR, Generator.BARRIER_NOT_EFFECTIVE, 500));
        lst.add(new ArmsTriple(Generator.ACCIDENT_CATASTROPHIC, Generator.BARRIER_EFFECTIVE, 50));
        lst.add(new ArmsTriple(Generator.ACCIDENT_CATASTROPHIC, Generator.BARRIER_LIMITED, 102));
        lst.add(new ArmsTriple(Generator.ACCIDENT_CATASTROPHIC, Generator.BARRIER_MINIMAL, 502));
        lst.add(new ArmsTriple(Generator.ACCIDENT_CATASTROPHIC, Generator.BARRIER_NOT_EFFECTIVE, 2500));
        return lst;
    }

    private static final class ArmsTriple {
        private final URI accidentOutcome;
        private final URI barrierEffectiveness;
        private final int expectedArmsIndex;

        private ArmsTriple(URI accidentOutcome, URI barrierEffectiveness, int expectedArmsIndex) {
            this.accidentOutcome = accidentOutcome;
            this.barrierEffectiveness = barrierEffectiveness;
            this.expectedArmsIndex = expectedArmsIndex;
        }
    }
}