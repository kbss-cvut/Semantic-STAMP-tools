package cz.cvut.kbss.inbas.audit.model.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.kbss.inbas.audit.model.HasOwlKey;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.util.IdentificationUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@OWLClass(iri = Vocabulary.PreliminaryReport)
public class PreliminaryReport implements HasOwlKey, Serializable, Report {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    @OWLDataProperty(iri = Vocabulary.p_dateCreated)
    private Date created;

    @OWLDataProperty(iri = Vocabulary.p_dateLastEdited)
    private Date lastEdited;

    @OWLDataProperty(iri = Vocabulary.p_revision)
    private Integer revision;

    @OWLDataProperty(iri = Vocabulary.p_severityLevel)
    private OccurrenceSeverity severityAssessment;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String summary;

    @OWLObjectProperty(iri = Vocabulary.p_hasAuthor, fetch = FetchType.EAGER)
    private Person author;

    @OWLObjectProperty(iri = Vocabulary.p_lastEditedBy, fetch = FetchType.EAGER)
    private Person lastEditedBy;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasOccurrence, fetch = FetchType.EAGER)
    private Occurrence occurrence;

    @OWLObjectProperty(iri = Vocabulary.p_hasInitialReport, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<InitialReport> initialReports;

    @OWLObjectProperty(iri = Vocabulary.p_hasCorrectiveMeasure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasure> correctiveMeasures;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventTypeAssessment, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<EventTypeAssessment> typeAssessments;

    public PreliminaryReport() {
        this.revision = 1;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
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

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Person getLastEditedBy() {
        return lastEditedBy;
    }

    public void setLastEditedBy(Person lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public Set<InitialReport> getInitialReports() {
        return initialReports;
    }

    public void setInitialReports(Set<InitialReport> initialReports) {
        this.initialReports = initialReports;
    }

    public OccurrenceSeverity getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(OccurrenceSeverity severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public Set<CorrectiveMeasure> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasure> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public Set<EventTypeAssessment> getTypeAssessments() {
        return typeAssessments;
    }

    public void setTypeAssessments(Set<EventTypeAssessment> typeAssessments) {
        this.typeAssessments = typeAssessments;
    }

    @JsonIgnore
    public Collection<ReportingStatement> getStatements() {
        final Set<ReportingStatement> statements = new HashSet<>(correctiveMeasures);
        statements.addAll(typeAssessments);
        return statements;
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
    public void generateKey() {
        if (key == null) {
            // Note: this either has to be called before persist or a setter has to be called to make JOPA notice the change
            this.key = IdentificationUtils.generateKey();
        }
    }

    @Override
    public String toString() {
        return "PreliminaryReport{" +
                "uri=" + uri +
                ", author=" + author +
                ", revision=" + revision +
                ", occurrence=" + occurrence +
                '}';
    }

    @Override
    public ReportingPhase getPhase() {
        return ReportingPhase.PRELIMINARY;
    }
}
