package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.model.AbstractEntity;
import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// TODO Request? We already have a different version of corrective measure request related to occurrence report
@OWLClass(iri = Vocabulary.s_c_corrective_measure_request)
public class CorrectiveMeasure extends AbstractEntity implements Serializable {

    @OWLDataProperty(iri = Vocabulary.s_p_description)
    private String description;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_responsible_person, fetch = FetchType.EAGER)
    private Set<Person> responsiblePersons;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_responsible_organization, fetch = FetchType.EAGER)
    private Set<Organization> responsibleOrganizations;

    @OWLDataProperty(iri = Vocabulary.s_p_deadline)
    private Date deadline;

    // TODO has reporting phase is probably not very suitable for corrective measure
    @OWLObjectProperty(iri = Vocabulary.s_p_has_reporting_phase)
    private URI phase;

    public CorrectiveMeasure() {
    }

    public CorrectiveMeasure(CorrectiveMeasure other) {
        this.description = other.description;
        this.deadline = other.deadline;
        this.phase = other.phase;
        if (other.responsiblePersons != null) {
            this.responsiblePersons = new HashSet<>(other.responsiblePersons);
        }
        if (other.responsibleOrganizations != null) {
            this.responsibleOrganizations = new HashSet<>(other.responsibleOrganizations);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Person> getResponsiblePersons() {
        return responsiblePersons;
    }

    public void setResponsiblePersons(Set<Person> responsiblePersons) {
        this.responsiblePersons = responsiblePersons;
    }

    public Set<Organization> getResponsibleOrganizations() {
        return responsibleOrganizations;
    }

    public void setResponsibleOrganizations(
            Set<Organization> responsibleOrganizations) {
        this.responsibleOrganizations = responsibleOrganizations;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public URI getPhase() {
        return phase;
    }

    public void setPhase(URI phase) {
        this.phase = phase;
    }

    @Override
    public String toString() {
        // First 50 characters of the description
        if (description != null) {
            return "CorrectiveMeasure{" +
                    (description.length() > 50 ? description.substring(0, 50) + "..." : description) + '}';
        }
        return "CorrectiveMeasure{" + uri + "}";
    }
}
