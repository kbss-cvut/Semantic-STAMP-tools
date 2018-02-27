package cz.cvut.kbss.reporting.model.reportlist;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.model.Vocabulary;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;

public class OccurrenceReportTest {

    @Test
    public void toOccurrenceReportCopiesAllAttributes() {
        final OccurrenceReport report = generateReport();
        final cz.cvut.kbss.reporting.model.OccurrenceReport result = report.toOccurrenceReport();
        assertNotNull(result);
        verify(report, result);
    }

    private OccurrenceReport generateReport() {
        final OccurrenceReport r = new OccurrenceReport();
        r.setUri(Generator.generateUri());
        r.setAuthor(Generator.getPerson());
        r.setDateCreated(new Date());
        r.setLastModified(new Date());
        r.setLastModifiedBy(r.getAuthor());
        r.setFileNumber(System.currentTimeMillis());
        r.setKey("117");
        r.setRevision(1);
        r.setSummary("Summary");
        r.setSeverityAssessment(Generator.generateUri());
        r.setTypes(Collections.singleton(Vocabulary.s_c_occurrence_report_A));

        final Occurrence occurrence = new Occurrence();
        occurrence.setName("TestOccurrence");
        occurrence.setStartTime(new Date());
        occurrence.setEndTime(new Date());
        occurrence.setEventType(Generator.generateEventType());
        occurrence.setKey("118");
        occurrence.setTypes(new HashSet<>(Arrays.asList(Vocabulary.s_c_event, occurrence.getEventType().toString())));
        occurrence.setUri(Generator.generateUri());
        r.setOccurrence(occurrence);
        return r;
    }

    private void verify(OccurrenceReport expected, cz.cvut.kbss.reporting.model.OccurrenceReport actual) {
        assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getFileNumber(), actual.getFileNumber());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getLastModifiedBy(), actual.getLastModifiedBy());
        assertEquals(expected.getDateCreated(), actual.getDateCreated());
        assertEquals(expected.getLastModified(), actual.getLastModified());
        assertEquals(expected.getPhase(), actual.getPhase());
        assertEquals(expected.getSummary(), actual.getSummary());
        assertEquals(expected.getSeverityAssessment(), actual.getSeverityAssessment());
        assertEquals(expected.getTypes(), actual.getTypes());
        assertEquals(expected.getRevision(), actual.getRevision());
        assertEquals(expected.getReferences(), actual.getReferences());
        assertNull(actual.getCorrectiveMeasures());
        assertNull(actual.getInitialReport());
        verify(expected.getOccurrence(), actual.getOccurrence());
    }

    private void verify(Occurrence expected, cz.cvut.kbss.reporting.model.Occurrence actual) {
        assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getStartTime(), actual.getStartTime());
        assertEquals(expected.getEndTime(), actual.getEndTime());
        assertEquals(expected.getEventType(), actual.getEventType());
        assertEquals(expected.getTypes(), actual.getTypes());
        assertNull(actual.getChildren());
        assertNull(actual.getFactors());
        assertNull(actual.getQuestion());
    }
}