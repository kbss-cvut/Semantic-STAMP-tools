package cz.cvut.kbss.reporting.model;

import cz.cvut.kbss.reporting.dto.reportlist.OccurrenceReportDto;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.environment.util.TestUtils;
import org.junit.Test;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class OccurrenceReportTest {

    @Test
    public void newInstanceContainsReportInTypes() {
        final OccurrenceReport report = new OccurrenceReport();
        assertTrue(report.getTypes().contains(Vocabulary.s_c_report));
    }

    @Test
    public void copyConstructorCopiesRelevantAttributes() {
        final OccurrenceReport original = OccurrenceReportGenerator.generateOccurrenceReport(true);
        original.setReadOnly(true);
        final OccurrenceReport copy = new OccurrenceReport(original);

        assertNull(copy.getUri());
        assertNull(copy.getKey());
        assertNull(copy.getDateCreated());
        assertNull(copy.getAuthor());
        assertNull(copy.getLastModified());
        assertNull(copy.getLastModifiedBy());

        assertEquals(original.getFileNumber(), copy.getFileNumber());
        assertEquals(original.getPhase(), copy.getPhase());
        assertEquals(original.getOccurrence().getName(), copy.getOccurrence().getName());
        assertEquals(original.getSummary(), copy.getSummary());
        assertEquals(original.getSeverityAssessment(), copy.getSeverityAssessment());
        assertEquals(original.getResponsibleDepartments(), copy.getResponsibleDepartments());
        assertEquals(original.isReadOnly(), copy.isReadOnly());
    }

    @Test
    public void copyConstructorCreatesCopyOfResponsibleDepartments() {
        final OccurrenceReport original = OccurrenceReportGenerator.generateOccurrenceReport(true);
        final Set<URI> departments = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            departments.add(Generator.generateUri());
        }
        original.setResponsibleDepartments(departments);
        final OccurrenceReport copy = new OccurrenceReport(original);
        assertEquals(original.getResponsibleDepartments(), copy.getResponsibleDepartments());
        assertNotSame(original.getResponsibleDepartments(), copy.getResponsibleDepartments());
    }

    @Test
    public void copyConstructorLeavesResponsibleDepartmentsNullWhenOriginalHasNone() {
        final OccurrenceReport original = OccurrenceReportGenerator.generateOccurrenceReport(true);
        original.setResponsibleDepartments(null);
        final OccurrenceReport copy = new OccurrenceReport(original);
        assertNull(copy.getResponsibleDepartments());
    }

    @Test
    public void copyConstructorCreatesNewCorrectiveMeasureRequests() {
        final OccurrenceReport original = OccurrenceReportGenerator.generateOccurrenceReport(true);
        final Set<CorrectiveMeasureRequest> requests = new HashSet<>();
        final CorrectiveMeasureRequest rOne = new CorrectiveMeasureRequest();
        rOne.setDescription("CorrectiveMeasureRequest_One");
        requests.add(rOne);
        final CorrectiveMeasureRequest rTwo = new CorrectiveMeasureRequest();
        rTwo.setDescription("CorrectiveMeasureRequest_Two");
        rTwo.setResponsiblePersons(Collections.singleton(Generator.getPerson()));
        requests.add(rTwo);
        original.setCorrectiveMeasures(requests);

        final OccurrenceReport copy = new OccurrenceReport(original);
        assertNotNull(copy.getCorrectiveMeasures());
        TestUtils.verifyCorrectiveMeasures(original.getCorrectiveMeasures(), copy.getCorrectiveMeasures());
    }

    @Test
    public void copyConstructorCopiesReferences() {
        final OccurrenceReport original = OccurrenceReportGenerator.generateOccurrenceReport(true);
        original.setReferences(new HashSet<>());
        for (int i = 0; i < Generator.randomInt(5, 10); i++) {
            final Resource ref = new Resource();
            ref.setReference(Generator.generateUri().toString());
            ref.setDescription("Resource " + i);
            original.getReferences().add(ref);
        }

        final OccurrenceReport copy = new OccurrenceReport(original);
        assertEquals(original.getReferences().size(), copy.getReferences().size());
        assertTrue(copy.getReferences().containsAll(original.getReferences()));
    }

    @Test
    public void testToReportDto() {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.setPhase(Generator.generateEventType());

        final ReportDto dto = report.toReportDto();
        assertTrue(dto instanceof OccurrenceReportDto);
        final OccurrenceReportDto result = (OccurrenceReportDto) dto;
        assertEquals(report.getUri(), result.getUri());
        assertEquals(report.getKey(), result.getKey());
        assertEquals(report.getFileNumber(), result.getFileNumber());
        assertEquals(report.getPhase(), result.getPhase());
        assertEquals(report.getAuthor(), result.getAuthor());
        assertEquals(report.getDateCreated(), result.getDateCreated());
        assertEquals(report.getLastModified(), result.getLastModified());
        assertEquals(report.getLastModifiedBy(), result.getLastModifiedBy());
        assertEquals(report.getRevision(), result.getRevision());
        assertEquals(report.getOccurrence().getName(), result.getIdentification());
        assertEquals(report.getOccurrence().getStartTime(), result.getDate());
        assertTrue(result.getTypes().containsAll(report.getTypes()));
        assertEquals(report.getSeverityAssessment(), result.getSeverityAssessment());
        assertEquals(report.getOccurrence().getEventTypes(), result.getOccurrenceCategories());
        assertEquals(report.getSummary(), result.getSummary());
    }

    @Test
    public void toReportDtoAddsOccurrenceReportToTypes() {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        final ReportDto dto = report.toReportDto();
        assertTrue(dto.getTypes().contains(Vocabulary.s_c_occurrence_report));
    }

    @Test
    public void copyConstructorCopiesArmsValues() {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        final OccurrenceReport copy = new OccurrenceReport(report);
        assertEquals(report.getAccidentOutcome(), copy.getAccidentOutcome());
        assertEquals(report.getBarrierEffectiveness(), copy.getBarrierEffectiveness());
    }
}
