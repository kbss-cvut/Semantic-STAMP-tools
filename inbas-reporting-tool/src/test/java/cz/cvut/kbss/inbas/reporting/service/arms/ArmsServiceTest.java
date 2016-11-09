package cz.cvut.kbss.inbas.reporting.service.arms;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueRiskAssessment;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.options.OptionsService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

public class ArmsServiceTest {

    private static final String INITIAL_EVENT_FREQUENCY_PREFIX = "http://onto.fel.cvut.cz/ontologies/arms/sira/model/initial-event-frequency/";
    private static final String BARRIER_UOS_AVOID_FAIL_FREQ_PREFIX = "http://onto.fel.cvut.cz/ontologies/arms/sira/model/barrier-uos-avoidance-fail-frequency/";
    private static final String BARRIER_RECOVERY_FAIL_FREQ_PREFIX = "http://onto.fel.cvut.cz/ontologies/arms/sira/model/barrier-recovery-fail-frequency/";
    private static final String ACCIDENT_SEVERITY_PREFIX = "http://onto.fel.cvut.cz/ontologies/arms/sira/model/accident-severity/";
    private static final String SIRA_PREFIX = "http://onto.fel.cvut.cz/ontologies/arms/sira/model/";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private OptionsService optionsService;

    @InjectMocks
    private ArmsServiceImpl armsService;

    private OccurrenceReport report;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        doReturn(new RawJson(Environment.loadData("data/accidentOutcome.json", String.class)))
                .when(optionsService).getOptions(eq("accidentOutcome"), anyMap());
        doReturn(new RawJson(Environment.loadData("data/barrierEffectiveness.json", String.class)))
                .when(optionsService).getOptions(eq("barrierEffectiveness"), anyMap());
        doReturn(new RawJson(Environment.loadData("option/initialEventFrequency.json", String.class)))
                .when(optionsService).getOptions(eq("initialEventFrequency"), anyMap());
        doReturn(new RawJson(Environment.loadData("option/barrierUosAvoidanceFailFrequency.json", String.class)))
                .when(optionsService).getOptions(eq("barrierUosAvoidanceFailFrequency"), anyMap());
        doReturn(new RawJson(Environment.loadData("option/barrierRecoveryFailFrequency.json", String.class)))
                .when(optionsService).getOptions(eq("barrierRecoveryFailFrequency"), anyMap());
        doReturn(new RawJson(Environment.loadData("option/accidentSeverity.json", String.class)))
                .when(optionsService).getOptions(eq("accidentSeverity"), anyMap());
        doReturn(new RawJson(Environment.loadData("option/sira.json", String.class)))
                .when(optionsService).getOptions(eq("sira"), anyMap());
    }

    @Test
    public void testArmsCalculationForReport() {
        armsService.initArmsAttributes();
        final List<ArmsTriple> testValues = initArmsTestValues();
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
        for (ArmsTriple at : initArmsTestValues()) {
            final int armsIndex = armsService.calculateArmsIndex(at.accidentOutcome, at.barrierEffectiveness);
            assertEquals(at.expectedArmsIndex, armsIndex);
        }
    }

    @Test
    public void calculateArmsReturnsZeroForMissingBarrierEffectiveness() {
        armsService.initArmsAttributes();
        report.setAccidentOutcome(OccurrenceReportGenerator.ACCIDENT_CATASTROPHIC);
        assertEquals(0, armsService.calculateArmsIndex(report));
    }

    @Test
    public void calculateArmsReturnsZeroForMissingAccidentOutcome() {
        armsService.initArmsAttributes();
        report.setBarrierEffectiveness(OccurrenceReportGenerator.BARRIER_LIMITED);
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
                .calculateArmsIndex(URI.create("http://unknownAccidentOutcome"),
                        OccurrenceReportGenerator.BARRIER_EFFECTIVE));
    }

    @Test
    public void calculateArmsReturnsZeroForUnknownBarrierEffectiveness() {
        armsService.initArmsAttributes();
        assertEquals(0, armsService
                .calculateArmsIndex(OccurrenceReportGenerator.ACCIDENT_MINOR,
                        URI.create("http://unknownBarrierEffectiveness")));
    }

    @Test
    public void calculateArmsReturnsZeroWhenItWasUnableToLoadArmsValues() {
        doReturn(new RawJson("")).when(optionsService).getOptions(eq("accidentOutcome"), anyMap());
        armsService.initArmsAttributes();
        assertEquals(0, armsService.calculateArmsIndex(OccurrenceReportGenerator.ACCIDENT_MINOR,
                OccurrenceReportGenerator.BARRIER_EFFECTIVE));
    }

    @Test
    public void calculateSafetyIssueRiskAssessmentReturnsNullForAnyMissingValue() {
        armsService.initArmsAttributes();
        SafetyIssueRiskAssessment sira = SafetyIssueReportGenerator.generateSira();
        sira.setInitialEventFrequency(null);
        assertNull(armsService.calculateSafetyIssueRiskAssessment(sira));

        sira = SafetyIssueReportGenerator.generateSira();
        sira.setBarrierUosAvoidanceFailFrequency(null);
        assertNull(armsService.calculateSafetyIssueRiskAssessment(sira));

        sira = SafetyIssueReportGenerator.generateSira();
        sira.setBarrierRecoveryFailFrequency(null);
        assertNull(armsService.calculateSafetyIssueRiskAssessment(sira));

        sira = SafetyIssueReportGenerator.generateSira();
        sira.setAccidentSeverity(null);
        assertNull(armsService.calculateSafetyIssueRiskAssessment(sira));
    }

    @Test
    public void testCalculateSafetyIssueRiskAssessment() throws Exception {
        armsService.initArmsAttributes();
        final List<SiraQuintuple> values = initSiraTestData();
        for (SiraQuintuple quintuple : values) {
            final SafetyIssueRiskAssessment sira = initSafetyIssueRiskAssessment(quintuple);
            final URI result = armsService.calculateSafetyIssueRiskAssessment(sira);
            assertNotNull(result);
            assertEquals("For values: " + sira, URI.create(quintuple.sira), result);
        }
    }

    @Test
    public void throwsIllegalArgumentWhenInitialEventFrequencyIsInvalidUri() throws Exception {
        armsService.initArmsAttributes();
        final SafetyIssueRiskAssessment sira = initSafetyIssueRiskAssessment(initSiraTestData().get(0));
        sira.setInitialEventFrequency(Generator.generateUri());
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("One of the risk assessment attribute values is invalid. " + sira);
        armsService.calculateSafetyIssueRiskAssessment(sira);
    }

    private SafetyIssueRiskAssessment initSafetyIssueRiskAssessment(SiraQuintuple quintuple) {
        final SafetyIssueRiskAssessment sira = new SafetyIssueRiskAssessment();
        sira.setInitialEventFrequency(URI.create(quintuple.initialEventFrequency));
        sira.setBarrierUosAvoidanceFailFrequency(URI.create(quintuple.barrierUosAvoidanceFailFrequency));
        sira.setBarrierRecoveryFailFrequency(URI.create(quintuple.barrierRecoveryFailFrequency));
        sira.setAccidentSeverity(URI.create(quintuple.accidentSeverity));
        return sira;
    }

    @Test
    public void throwsIllegalArgumentWhenBarrierUOSAvoidanceFailFrequencyIsInvalidUri() throws Exception {
        armsService.initArmsAttributes();
        final SafetyIssueRiskAssessment sira = initSafetyIssueRiskAssessment(initSiraTestData().get(0));
        sira.setBarrierUosAvoidanceFailFrequency(Generator.generateUri());
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("One of the risk assessment attribute values is invalid. " + sira);
        armsService.calculateSafetyIssueRiskAssessment(sira);
    }

    @Test
    public void throwsIllegalArgumentWhenBarrierRecoveryFailFrequencyIsInvalidUri() throws Exception {
        armsService.initArmsAttributes();
        final SafetyIssueRiskAssessment sira = initSafetyIssueRiskAssessment(initSiraTestData().get(0));
        sira.setBarrierRecoveryFailFrequency(Generator.generateUri());
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("One of the risk assessment attribute values is invalid. " + sira);
        armsService.calculateSafetyIssueRiskAssessment(sira);
    }

    @Test
    public void throwsIllegalArgumentWhenAccidentSeverityIsInvalidUri() throws Exception {
        armsService.initArmsAttributes();
        final SafetyIssueRiskAssessment sira = initSafetyIssueRiskAssessment(initSiraTestData().get(0));
        sira.setAccidentSeverity(Generator.generateUri());
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("One of the risk assessment attribute values is invalid. " + sira);
        armsService.calculateSafetyIssueRiskAssessment(sira);
    }

    @Test
    public void initializesSiraValuesToEmptyCollectionsWhenInitializationFromDataFails() throws Exception {
        doReturn(new RawJson("")).when(optionsService).getOptions(eq("accidentOutcome"), anyMap());
        armsService.initArmsAttributes();
        assertTrue(((Map) getFieldContent("initialEventFrequencies")).isEmpty());
        assertTrue(((Map) getFieldContent("barrierUosAvoidanceFailFrequencies")).isEmpty());
        assertTrue(((Map) getFieldContent("barrierRecoveryFailFrequencies")).isEmpty());
        assertTrue(((Map) getFieldContent("accidentSeverities")).isEmpty());
        assertTrue(((List) getFieldContent("siraValues")).isEmpty());
    }

    private Object getFieldContent(String fieldName) throws Exception {
        final Field field = ArmsServiceImpl.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(armsService);
    }

    @Test
    public void siraCalculationReturnsNullWhenSiraValuesAreNotInitialized() throws Exception {
        doReturn(new RawJson("")).when(optionsService).getOptions(eq("accidentOutcome"), anyMap());
        armsService.initArmsAttributes();
        final SafetyIssueRiskAssessment sira = initSafetyIssueRiskAssessment(initSiraTestData().get(0));

        assertNull(armsService.calculateSafetyIssueRiskAssessment(sira));
    }

    /**
     * ARMS values according to https://essi.easa.europa.eu/documents/Methodology.pdf, page 19.
     */
    private List<ArmsTriple> initArmsTestValues() {
        final List<ArmsTriple> lst = new ArrayList<>();
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_NEGLIGIBLE,
                OccurrenceReportGenerator.BARRIER_EFFECTIVE, 1));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_NEGLIGIBLE, OccurrenceReportGenerator.BARRIER_LIMITED,
                1));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_NEGLIGIBLE, OccurrenceReportGenerator.BARRIER_MINIMAL,
                1));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_NEGLIGIBLE,
                OccurrenceReportGenerator.BARRIER_NOT_EFFECTIVE, 1));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_MINOR, OccurrenceReportGenerator.BARRIER_EFFECTIVE,
                2));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_MINOR, OccurrenceReportGenerator.BARRIER_LIMITED,
                (short) 4));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_MINOR, OccurrenceReportGenerator.BARRIER_MINIMAL,
                20));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_MINOR,
                OccurrenceReportGenerator.BARRIER_NOT_EFFECTIVE, 100));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_MAJOR, OccurrenceReportGenerator.BARRIER_EFFECTIVE,
                10));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_MAJOR, OccurrenceReportGenerator.BARRIER_LIMITED,
                21));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_MAJOR, OccurrenceReportGenerator.BARRIER_MINIMAL,
                101));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_MAJOR,
                OccurrenceReportGenerator.BARRIER_NOT_EFFECTIVE, 500));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_CATASTROPHIC,
                OccurrenceReportGenerator.BARRIER_EFFECTIVE, 50));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_CATASTROPHIC,
                OccurrenceReportGenerator.BARRIER_LIMITED, 102));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_CATASTROPHIC,
                OccurrenceReportGenerator.BARRIER_MINIMAL, 502));
        lst.add(new ArmsTriple(OccurrenceReportGenerator.ACCIDENT_CATASTROPHIC,
                OccurrenceReportGenerator.BARRIER_NOT_EFFECTIVE, 2500));
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

    private List<SiraQuintuple> initSiraTestData() throws Exception {
        final List<SiraQuintuple> lst = new ArrayList<>();
        try (final BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("data/siraTestData.csv")))) {
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                final String[] values = line.split(",");
                lst.add(new SiraQuintuple(INITIAL_EVENT_FREQUENCY_PREFIX + values[0],
                        BARRIER_UOS_AVOID_FAIL_FREQ_PREFIX + values[1],
                        BARRIER_RECOVERY_FAIL_FREQ_PREFIX + values[2], ACCIDENT_SEVERITY_PREFIX + values[3],
                        SIRA_PREFIX + values[4]));
            }
        }
        return lst;
    }

    private static final class SiraQuintuple {
        private final String initialEventFrequency;
        private final String barrierUosAvoidanceFailFrequency;
        private final String barrierRecoveryFailFrequency;
        private final String accidentSeverity;
        private final String sira;

        private SiraQuintuple(String initialEventFrequency, String barrierUosAvoidanceFailFrequency,
                              String barrierRecoveryFailFrequency, String accidentSeverity, String sira) {
            this.initialEventFrequency = initialEventFrequency;
            this.barrierUosAvoidanceFailFrequency = barrierUosAvoidanceFailFrequency;
            this.barrierRecoveryFailFrequency = barrierRecoveryFailFrequency;
            this.accidentSeverity = accidentSeverity;
            this.sira = sira;
        }
    }
}
