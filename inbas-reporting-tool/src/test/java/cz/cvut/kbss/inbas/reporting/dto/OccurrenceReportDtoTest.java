package cz.cvut.kbss.inbas.reporting.dto;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.rest.dto.mapper.DtoMapper;
import cz.cvut.kbss.inbas.reporting.rest.dto.mapper.DtoMapperImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class OccurrenceReportDtoTest {

    private DtoMapper dtoMapper = new DtoMapperImpl();

    @Test
    public void testToReportDto() {
        final OccurrenceReportDto dto = dtoMapper
                .occurrenceReportToOccurrenceReportDto(Generator.generateOccurrenceReport(true));

        final ReportDto target = dto.toReportDto();
        assertTrue(target instanceof cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto);
        final cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto result = (cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto) target;
        assertEquals(dto.getUri(), result.getUri());
        assertEquals(dto.getKey(), result.getKey());
        assertEquals(dto.getFileNumber(), result.getFileNumber());
        assertEquals(dto.getAuthor(), result.getAuthor());
        assertEquals(dto.getDateCreated(), result.getDateCreated());
        assertEquals(dto.getLastModified(), result.getLastModified());
        assertEquals(dto.getLastModifiedBy(), result.getLastModifiedBy());
        assertEquals(dto.getRevision(), result.getRevision());
        assertEquals(dto.getOccurrence().getName(), result.getIdentification());
        assertEquals(dto.getOccurrence().getStartTime(), result.getDate());
        assertEquals(dto.getTypes(), result.getTypes());
        assertEquals(dto.getSeverityAssessment(), result.getSeverityAssessment());
        assertEquals(dto.getSummary(), result.getSummary());
    }

    @Test
    public void toReportDtoHandlesNullTypes() {
        final OccurrenceReportDto dto = dtoMapper
                .occurrenceReportToOccurrenceReportDto(Generator.generateOccurrenceReport(true));
        dto.setTypes(null);
        final ReportDto target = dto.toReportDto();
        assertTrue(target instanceof cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto);
        assertNull(target.getTypes());
    }
}