package cz.cvut.kbss.inbas.reporting.model.audit;

import cz.cvut.kbss.inbas.reporting.model.AbstractEntity;
import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.qam.Question;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.s_c_Audit)
public class Audit extends AbstractEntity implements Serializable {

    @ParticipationConstraints(nonEmpty = true)
    @OWLAnnotationProperty(iri = Vocabulary.s_p_label)
    private String name;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_start_time)
    private Date startDate;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_end_time)
    private Date endDate;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_has_auditee, fetch = FetchType.EAGER)
    private Organization auditee;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_location)
    private URI location;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_question, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Question question;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_part, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<AuditFinding> findings;

    @Types
    private Set<String> types;

    public Audit() {
        this.types = new HashSet<>(4);
        types.add(Vocabulary.s_c_Event);
    }

    public Audit(Audit other) {
        Objects.requireNonNull(other);
        this.name = other.name;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.location = other.location;
        this.auditee = other.auditee;
        this.types = new HashSet<>(other.types);
        this.findings =
                other.findings != null ? other.findings.stream().map(AuditFinding::new).collect(Collectors.toSet()) :
                null;
        this.question = other.question != null ? new Question(other.question) : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Organization getAuditee() {
        return auditee;
    }

    public void setAuditee(Organization auditee) {
        this.auditee = auditee;
    }

    public URI getLocation() {
        return location;
    }

    public void setLocation(URI location) {
        this.location = location;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<AuditFinding> getFindings() {
        return findings;
    }

    public void setFindings(Set<AuditFinding> findings) {
        this.findings = findings;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "name='" + name + '\'' +
                ", startDate=" + startDate +
                ", auditee=" + auditee +
                ", types=" + types +
                '}';
    }
}
