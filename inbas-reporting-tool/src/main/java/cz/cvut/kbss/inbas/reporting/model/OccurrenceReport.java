package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.model.arms.AccidentOutcome;
import cz.cvut.kbss.inbas.reporting.model.arms.BarrierEffectiveness;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.OccurrenceReport)
public class OccurrenceReport implements LogicalDocument, Serializable {

    @Id(generated = true)
    private URI uri;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    /**
     * File number identifies a particular report chain, i.e. revisions of the same report.
     */
    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_fileNumber)
    private Long fileNumber;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_documents, fetch = FetchType.EAGER)
    private Occurrence occurrence;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasAuthor, fetch = FetchType.EAGER)
    private Person author;

    @OWLDataProperty(iri = Vocabulary.p_dateCreated)
    private Date dateCreated;

    @OWLDataProperty(iri = Vocabulary.p_lastModified)
    private Date lastModified;

    @OWLObjectProperty(iri = Vocabulary.p_lastModifiedBy, fetch = FetchType.EAGER)
    private Person lastModifiedBy;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_revision)
    private Integer revision;

    @OWLObjectProperty(iri = Vocabulary.p_severityAssessment)
    private URI severityAssessment;

    @OWLObjectProperty(iri = Vocabulary.p_hasCorrectiveMeasure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasureRequest> correctiveMeasures;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String summary;

    // ARMS Attributes

    @OWLDataProperty(iri = Vocabulary.p_mostProbableAccidentOutcome)
    private AccidentOutcome accidentOutcome;

    @OWLDataProperty(iri = Vocabulary.p_barrierEffectiveness)
    private BarrierEffectiveness barrierEffectiveness;

    @Transient
    private Short armsIndex;

    @Types
    private Set<String> types;

    public OccurrenceReport() {
        this.types = new HashSet<>(4);
        types.add(Vocabulary.Report);
    }

    public OccurrenceReport(OccurrenceReport other) {
        this();
        Objects.requireNonNull(other);
        this.fileNumber = other.fileNumber;
        // TODO Copy occurrence?
        this.occurrence = other.occurrence;
        this.severityAssessment = other.severityAssessment;
        this.summary = other.summary;
        if (other.correctiveMeasures != null) {
            this.correctiveMeasures = other.correctiveMeasures.stream().map(CorrectiveMeasureRequest::new)
                                                              .collect(Collectors.toSet());
        }
    }

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(Long fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Person getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Person lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

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

    public Set<CorrectiveMeasureRequest> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasureRequest> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public AccidentOutcome getAccidentOutcome() {
        return accidentOutcome;
    }

    public void setAccidentOutcome(AccidentOutcome accidentOutcome) {
        this.accidentOutcome = accidentOutcome;
    }

    public BarrierEffectiveness getBarrierEffectiveness() {
        return barrierEffectiveness;
    }

    public void setBarrierEffectiveness(BarrierEffectiveness barrierEffectiveness) {
        this.barrierEffectiveness = barrierEffectiveness;
    }

    public Short getArmsIndex() {
        return armsIndex;
    }

    public void setArmsIndex(Short armsIndex) {
        this.armsIndex = armsIndex;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "OccurrenceReport{" +
                "uri=" + uri +
                ", fileNumber=" + fileNumber +
                ", revision=" + revision +
                ", occurrence=" + occurrence +
                '}';
    }

    @Override
    public ReportDto toReportDto() {
        final OccurrenceReportDto res = new OccurrenceReportDto();
        res.setUri(uri);
        res.setKey(key);
        res.setFileNumber(fileNumber);
        res.setAuthor(author);
        res.setDateCreated(dateCreated);
        res.setLastModifiedBy(lastModifiedBy);
        res.setLastModified(lastModified);
        res.setRevision(revision);
        res.setTypes(types != null ? new HashSet<>(types) : new HashSet<>());
        res.getTypes().add(Vocabulary.OccurrenceReport);
        assert occurrence != null;
        res.setIdentification(occurrence.getName());
        res.setDate(occurrence.getStartTime());
        res.setSummary(summary);
        res.setSeverityAssessment(severityAssessment);
        res.setOccurrenceCategory(occurrence.getEventType());
        return res;
    }
}
