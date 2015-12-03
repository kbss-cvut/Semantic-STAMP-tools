package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Set;

/**
 * Represents an occurrence report.
 *
 * All reports (initial, preliminary, investigation) are kinds of OccurrenceReport. This is currently represented
 * only on the ontology level by the specialized reports having also type {@link Vocabulary#Report}.
 *
 * This class is meant only for reading, all modifications should be done through the appropriate report instances.
 */
@OWLClass(iri = Vocabulary.Report)
public class OccurrenceReport implements Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_hasKey, readOnly = true)
    private String key;

    @OWLDataProperty(iri = Vocabulary.p_revision, readOnly = true)
    private Integer revision;

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

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
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
