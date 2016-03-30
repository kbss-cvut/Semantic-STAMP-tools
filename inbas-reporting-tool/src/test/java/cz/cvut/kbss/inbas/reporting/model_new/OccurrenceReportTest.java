package cz.cvut.kbss.inbas.reporting.model_new;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class OccurrenceReportTest {

    @Test
    public void newInstanceContainsReportInTypes() {
        final OccurrenceReport report = new OccurrenceReport();
        assertTrue(report.getTypes().contains(Vocabulary.Report));
    }

    @Test
    public void copyConstructorCopiesRelevantAttributes() {
        final OccurrenceReport original = Generator.generateOccurrenceReport(true);
        final OccurrenceReport copy = new OccurrenceReport(original);

        assertNull(copy.getUri());
        assertNull(copy.getKey());
        assertNull(copy.getDateCreated());
        assertNull(copy.getAuthor());
        assertNull(copy.getLastModified());
        assertNull(copy.getLastModifiedBy());

        assertEquals(original.getFileNumber(), copy.getFileNumber());
        assertEquals(original.getOccurrenceStart(), copy.getOccurrenceStart());
        assertEquals(original.getOccurrenceEnd(), copy.getOccurrenceEnd());
        assertEquals(original.getOccurrence(), copy.getOccurrence());
    }

    @Test
    public void copyConstructorCreatesNewCorrectiveMeasureRequests() {
        final OccurrenceReport original = Generator.generateOccurrenceReport(true);
        final Set<CorrectiveMeasureRequest> requests = new HashSet<>();
        final CorrectiveMeasureRequest rOne = new CorrectiveMeasureRequest();
        rOne.setDescription("CorrectiveMeasureRequest_One");
        requests.add(rOne);
        final CorrectiveMeasureRequest rTwo = new CorrectiveMeasureRequest();
        rTwo.setDescription("CorrectiveMeasureRequest_Two");
        rTwo.setResponsibleAgents(Collections.singleton(new Agent()));
        requests.add(rTwo);
        original.setCorrectiveMeasureRequests(requests);

        final OccurrenceReport copy = new OccurrenceReport(original);
        assertNotNull(copy.getCorrectiveMeasureRequests());
        assertEquals(original.getCorrectiveMeasureRequests().size(), copy.getCorrectiveMeasureRequests().size());
        for (CorrectiveMeasureRequest r : original.getCorrectiveMeasureRequests()) {
            for (CorrectiveMeasureRequest rr : copy.getCorrectiveMeasureRequests()) {
                assertNotSame(r, rr);
            }
        }
    }
}