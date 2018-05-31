package cz.cvut.kbss.reporting.model.reportlist;

import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import cz.cvut.kbss.reporting.dto.reportlist.OccurrenceReportDto;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.model.AbstractOccurrenceReport;
import cz.cvut.kbss.reporting.model.Vocabulary;

@OWLClass(iri = Vocabulary.s_c_occurrence_report)
public class OccurrenceReport extends AbstractOccurrenceReport {

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_documents, fetch = FetchType.EAGER)
    private Occurrence occurrence;

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    @Override
    public ReportDto toReportDto() {
        final OccurrenceReportDto dto = new OccurrenceReportDto();
        copyBasicAttributesToDto(dto);
        dto.setPhase(phase);
        dto.getTypes().add(Vocabulary.s_c_occurrence_report);
        assert occurrence != null;
        dto.setIdentification(occurrence.getName());
        dto.setDate(occurrence.getStartTime());
        dto.setSummary(summary);
        dto.setSeverityAssessment(severityAssessment);
        dto.setOccurrenceCategory(occurrence.getEventType());
        return dto;
    }

    public cz.cvut.kbss.reporting.model.OccurrenceReport toOccurrenceReport() {
        final cz.cvut.kbss.reporting.model.OccurrenceReport report = new cz.cvut.kbss.reporting.model.OccurrenceReport();
        report.setUri(uri);
        report.setKey(key);
        report.setFileNumber(fileNumber);
        report.setAuthor(author);
        report.setLastModifiedBy(lastModifiedBy);
        report.setDateCreated(dateCreated);
        report.setLastModified(lastModified);
        report.setRevision(revision);
        report.setSummary(summary);
        report.setSeverityAssessment(severityAssessment);
        report.setPhase(phase);
        report.setTypes(types);
        report.setReferences(references);
        report.setOccurrence(occurrence.toOccurrence());
        return report;
    }
}
