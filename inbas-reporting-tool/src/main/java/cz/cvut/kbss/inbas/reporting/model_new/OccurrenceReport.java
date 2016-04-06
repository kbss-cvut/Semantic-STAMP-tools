package cz.cvut.kbss.inbas.reporting.model_new;

import cz.cvut.kbss.inbas.reporting.model_new.arms.AccidentOutcome;
import cz.cvut.kbss.inbas.reporting.model_new.arms.BarrierEffectiveness;
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
    @OWLObjectProperty(iri = Vocabulary.p_documents, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Occurrence occurrence;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_startTime)
    private Date occurrenceStart;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_endTime)
    private Date occurrenceEnd;

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

    @OWLObjectProperty(iri = Vocabulary.p_severityAssessment, fetch = FetchType.EAGER)
    private SeverityLevel severityAssessment;

    @OWLObjectProperty(iri = Vocabulary.p_hasCorrectiveMeasure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasureRequest> correctiveMeasureRequests;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String summary;

    // ARMS Attributes

    @OWLDataProperty(iri = Vocabulary.p_mostProbableAccidentOutcome)
    private AccidentOutcome accidentOutcome;

    @OWLDataProperty(iri = Vocabulary.p_barrierEffectiveness)
    private BarrierEffectiveness barrierEffectiveness;

    @OWLDataProperty(iri = Vocabulary.p_armsIndex)
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
        this.occurrence = other.occurrence;
        this.occurrenceStart = other.occurrenceStart;
        this.occurrenceEnd = other.occurrenceEnd;
        this.severityAssessment = other.severityAssessment; // SeverityLevel instances are predefined
        if (other.correctiveMeasureRequests != null) {
            this.correctiveMeasureRequests = other.correctiveMeasureRequests.stream().map(CorrectiveMeasureRequest::new)
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

    public Date getOccurrenceStart() {
        return occurrenceStart;
    }

    public void setOccurrenceStart(Date occurrenceStart) {
        this.occurrenceStart = occurrenceStart;
    }

    public Date getOccurrenceEnd() {
        return occurrenceEnd;
    }

    public void setOccurrenceEnd(Date occurrenceEnd) {
        this.occurrenceEnd = occurrenceEnd;
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

    public SeverityLevel getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(SeverityLevel severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public Set<CorrectiveMeasureRequest> getCorrectiveMeasureRequests() {
        return correctiveMeasureRequests;
    }

    public void setCorrectiveMeasureRequests(Set<CorrectiveMeasureRequest> correctiveMeasureRequests) {
        this.correctiveMeasureRequests = correctiveMeasureRequests;
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
}
