package cz.cvut.kbss.inbas.reporting.service.formgen;

import cz.cvut.kbss.inbas.reporting.environment.config.PropertyMockingApplicationContextInitializer;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.formgen.OccurrenceReportFormGenDao;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@PropertySource("classpath:config.properties")
@ContextConfiguration(initializers = PropertyMockingApplicationContextInitializer.class)
public class EventFormGenDataProcessorTest extends BaseServiceTestRunner {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private OccurrenceReportFormGenDao dao;

    private EventFormGenDataProcessor processor;

    @Before
    public void setUp() {
        this.processor = new EventFormGenDataProcessor(dao);
    }

    @Test
    public void addsUriOfEventUsedForFormGenerationToParams() throws Exception {
        final OccurrenceReport report = getOccurrenceReport();
        final Event evt = report.getOccurrence().getChildren().iterator().next();
        final Integer referenceId = Generator.randomInt();
        evt.setReferenceId(referenceId);
        processor.process(report,
                Collections.singletonMap(EventFormGenDataProcessor.EVENT_PARAM, referenceId.toString()));
        checkForUri(evt.getUri());
    }

    private void checkForUri(URI uri) {
        final Map<String, String> params = processor.getParams();
        assertEquals(1, params.size());
        assertEquals(uri.toString(), params.get(EventFormGenDataProcessor.EVENT_PARAM));
    }

    private OccurrenceReport getOccurrenceReport() {
        final OccurrenceReport report = Generator.generateOccurrenceReportWithFactorGraph();
        report.getAuthor().generateUri();
        return report;
    }

    @Test
    public void processAddsUriOfOccurrenceUsedForFormGenerationToParams() throws Exception {
        final OccurrenceReport report = getOccurrenceReport();
        final Occurrence occurrence = report.getOccurrence();
        final Integer referenceId = Generator.randomInt();
        occurrence.setReferenceId(referenceId);
        processor.process(report,
                Collections.singletonMap(EventFormGenDataProcessor.EVENT_PARAM, referenceId.toString()));
        checkForUri(occurrence.getUri());
    }

    @Test
    public void processDoesNothingWhenReferenceIdIsMissing() throws Exception {
        final OccurrenceReport report = getOccurrenceReport();
        processor.process(report, Collections.emptyMap());
        assertTrue(processor.getParams().isEmpty());
    }

    @Test
    public void processThrowsIllegalArgumentWhenReferenceIdIsNotParseable() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        final String invalidReferenceId = "invalidOne";
        thrown.expectMessage("Event reference id " + invalidReferenceId + " is not valid.");
        final OccurrenceReport report = getOccurrenceReport();
        processor.process(report, Collections.singletonMap(EventFormGenDataProcessor.EVENT_PARAM, invalidReferenceId));
    }

    @Test
    public void processThrowsIllegalArgumentWhenReferenceIdIsNotFoundInFactorGraph() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        final Integer referenceId = Generator.randomInt();
        thrown.expectMessage("Event with reference id " + referenceId + " not found in the factor graph.");
        final OccurrenceReport report = getOccurrenceReport();
        processor.process(report,
                Collections.singletonMap(EventFormGenDataProcessor.EVENT_PARAM, referenceId.toString()));
    }
}