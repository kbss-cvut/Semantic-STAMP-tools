package cz.cvut.kbss.inbas.reporting.model.audit;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.AuditReportDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuditReportTest {

    @Test
    public void toDtoCopiesAttributesToSafetyIssueReportDto() {
        final AuditReport original = AuditReportGenerator.generateAuditReport(true);
        original.setUri(Generator.generateUri());
        final ReportDto dto = original.toReportDto();
        assertTrue(dto instanceof AuditReportDto);
        final AuditReportDto result = (AuditReportDto) dto;
        assertEquals(original.getUri(), result.getUri());
        assertEquals(original.getKey(), result.getKey());
        assertEquals(original.getFileNumber(), result.getFileNumber());
        assertEquals(original.getAuthor(), result.getAuthor());
        assertEquals(original.getDateCreated(), result.getDateCreated());
        assertEquals(original.getLastModifiedBy(), result.getLastModifiedBy());
        assertEquals(original.getLastModified(), result.getLastModified());
        assertEquals(original.getSummary(), result.getSummary());
        assertEquals(original.getRevision(), result.getRevision());
        assertEquals(original.getAudit().getName(), result.getIdentification());
        assertTrue(result.getTypes().containsAll(original.getTypes()));
    }

    @Test
    public void toDtoAddsClassIriToDtoTypes() {
        final AuditReport original = AuditReportGenerator.generateAuditReport(true);
        final ReportDto dto = original.toReportDto();
        assertTrue(dto.getTypes().contains(Vocabulary.s_c_audit_report));
    }

    @Test
    public void copyConstructorCopiesAudit() {
        final AuditReport original = AuditReportGenerator.generateAuditReport(true);

        final AuditReport copy = new AuditReport(original);
        assertNotNull(copy.getAudit());
        assertNotSame(original.getAudit(), copy.getAudit());
    }
}
