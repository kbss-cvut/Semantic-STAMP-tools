package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.util.HasUri;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Set;

// TODO Request? We already have a different version of corrective measure request related to occurrence report
@OWLClass(iri = Vocabulary.s_c_corrective_measure_request)
public class CorrectiveMeasure implements HasUri, Serializable {

    @Id(generated = true)
    private URI uri;

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

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
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
