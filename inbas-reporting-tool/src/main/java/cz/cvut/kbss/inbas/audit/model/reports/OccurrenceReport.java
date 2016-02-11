package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Set;

/**
 * Represents an occurrence report.
 * <p>
 * All reports (initial, preliminary, investigation) are kinds of OccurrenceReport. This is currently represented only
 * on the ontology level by the specialized reports having also type {@link Vocabulary#Report}.
 * <p>
 * This class is meant only for reading, all modifications should be done through the appropriate report instances.
 */
@OWLClass(iri = Vocabulary.Report)
public class OccurrenceReport implements Report, Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_hasKey, readOnly = true)
    private String key;

    @OWLDataProperty(iri = Vocabulary.p_fileNumber, readOnly = true)
    private Long fileNumber;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_occurrenceCategory, fetch = FetchType.EAGER, readOnly = true)
    private EventType occurrenceCategory;

    @OWLDataProperty(iri = Vocabulary.p_revision, readOnly = true)
    private Integer revision;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_startTime)
    private Date occurrenceStart;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_endTime)
    private Date occurrenceEnd;

    @OWLDataProperty(iri = Vocabulary.p_dateCreated, readOnly = true)
    private Date created;

    @OWLDataProperty(iri = Vocabulary.p_dateLastEdited, readOnly = true)
    private Date lastEdited;

    @OWLDataProperty(iri = Vocabulary.p_description, readOnly = true)
    private String summary;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasOccurrence, fetch = FetchType.EAGER, readOnly = true)
    private Occurrence occurrence;

    @Types(fetchType = FetchType.EAGER, readOnly = true)
    private Set<String> types;

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

    public EventType getOccurrenceCategory() {
        return occurrenceCategory;
    }

    public void setOccurrenceCategory(EventType occurrenceCategory) {
        this.occurrenceCategory = occurrenceCategory;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public ReportingPhase getPhase() {
        if (getTypes() != null && !getTypes().isEmpty()) {
            if (types.contains(Vocabulary.InvestigationReport)) {
                return ReportingPhase.INVESTIGATION;
            }
            if (types.contains(Vocabulary.PreliminaryReport)) {
                return ReportingPhase.PRELIMINARY;
            }
        }
        throw new IllegalStateException("Missing type specifying reporting phase. Types: " + types);
    }

    @Override
    public String toString() {
        return "OccurrenceReport{" +
                "uri=" + uri +
                ", revision=" + revision +
                ", lastEdited=" + lastEdited +
                ", summary='" + summary + '\'' +
                ", occurrence=" + occurrence +
                '}';
    }
}
