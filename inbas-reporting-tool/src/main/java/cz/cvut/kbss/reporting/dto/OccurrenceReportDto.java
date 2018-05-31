package cz.cvut.kbss.reporting.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.reporting.dto.event.FactorGraph;
import cz.cvut.kbss.reporting.dto.event.OccurrenceDto;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.model.*;
import cz.cvut.kbss.reporting.rest.dto.model.FormGenData;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// It is important that occurrence comes before factorGraph, because it defines a reference to the occurrence, which can the be used
@JsonPropertyOrder(value = {"uri, key, occurrence, factorGraph"})
@OWLClass(iri = Vocabulary.s_c_occurrence_report)
public class OccurrenceReportDto implements LogicalDocument, FormGenData {

    @Id
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.s_p_has_key)
    private String key;

    @OWLDataProperty(iri = Vocabulary.s_p_has_file_number)
    private Long fileNumber;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_reporting_phase)
    private URI phase;

    @OWLObjectProperty(iri = Vocabulary.s_p_documents)
    private OccurrenceDto occurrence;

    @OWLObjectProperty(iri = Vocabulary.s_p_based_on)
    private InitialReport initialReport;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_factor)
    private FactorGraph factorGraph;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_author)
    private Person author;

    @OWLDataProperty(iri = Vocabulary.s_p_created)
    private Date dateCreated;

    @OWLDataProperty(iri = Vocabulary.s_p_modified)
    private Date lastModified;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_last_editor)
    private Person lastModifiedBy;

    @OWLDataProperty(iri = Vocabulary.s_p_has_revision)
    private Integer revision;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_severity_assessment)
    private URI severityAssessment;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_corrective_measure)
    private Set<CorrectiveMeasureRequestDto> correctiveMeasures;

    @OWLAnnotationProperty(iri = Vocabulary.s_p_description)
    private String summary;

    @OWLObjectProperty(iri = Vocabulary.s_p_references)
    private Set<Resource> references;

    @Types
    private Set<String> types;

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Long getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(Long fileNumber) {
        this.fileNumber = fileNumber;
    }

    public URI getPhase() {
        return phase;
    }

    public void setPhase(URI phase) {
        this.phase = phase;
    }

    public OccurrenceDto getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(OccurrenceDto occurrence) {
        this.occurrence = occurrence;
    }

    public InitialReport getInitialReport() {
        return initialReport;
    }

    public void setInitialReport(InitialReport initialReport) {
        this.initialReport = initialReport;
    }

    public FactorGraph getFactorGraph() {
        return factorGraph;
    }

    public void setFactorGraph(FactorGraph factorGraph) {
        this.factorGraph = factorGraph;
    }

    @Override
    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Person getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Person lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public URI getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(URI severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public Set<CorrectiveMeasureRequestDto> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasureRequestDto> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public Set<Resource> getReferences() {
        return references;
    }

    public void setReferences(Set<Resource> references) {
        this.references = references;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public ReportDto toReportDto() {
        final cz.cvut.kbss.reporting.dto.reportlist.OccurrenceReportDto res = new cz.cvut.kbss.reporting.dto.reportlist.OccurrenceReportDto();
        res.setUri(uri);
        res.setKey(key);
        res.setFileNumber(fileNumber);
        res.setPhase(phase);
        res.setAuthor(author);
        res.setDateCreated(dateCreated);
        res.setLastModifiedBy(lastModifiedBy);
        res.setLastModified(lastModified);
        res.setRevision(revision);
        res.setTypes(types != null ? new HashSet<>(types) : new HashSet<>());
        res.getTypes().add(Vocabulary.s_c_occurrence_report);
        assert occurrence != null;
        res.setIdentification(occurrence.getName());
        res.setDate(occurrence.getStartTime());
        res.setSummary(summary);
        res.setSeverityAssessment(severityAssessment);
        res.setOccurrenceCategory(occurrence.getEventType());
        return res;
    }
}
