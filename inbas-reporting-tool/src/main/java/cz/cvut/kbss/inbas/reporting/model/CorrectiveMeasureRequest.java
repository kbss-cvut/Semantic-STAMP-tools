package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The responsiblePerson/Organization and basedOnEvent/Occurrence fields are here because of the lack of support for
 * inheritance in JOPA. This should be handled on DTO level, where these fields should be replaced with ones using
 * inheritance between agent - Person/Organization and Event - Occurrence.
 */
@OWLClass(iri = Vocabulary.s_c_corrective_measure_request)
public class CorrectiveMeasureRequest extends AbstractEntity implements Serializable {

    @OWLDataProperty(iri = Vocabulary.s_p_description)
    private String description;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_responsible_person, fetch = FetchType.EAGER)
    private Set<Person> responsiblePersons;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_responsible_organization, fetch = FetchType.EAGER)
    private Set<Organization> responsibleOrganizations;

    @OWLObjectProperty(iri = Vocabulary.s_p_based_on_event, fetch = FetchType.EAGER)
    private Event basedOnEvent;

    @OWLObjectProperty(iri = Vocabulary.s_p_based_on_occurrence, fetch = FetchType.EAGER)
    private Occurrence basedOnOccurrence;

    // Safety issue attributes

    @OWLDataProperty(iri = Vocabulary.s_p_deadline)
    private Date deadline;

    // TODO has reporting phase is probably not very suitable for corrective measure
    @OWLObjectProperty(iri = Vocabulary.s_p_has_reporting_phase)
    private URI phase;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_evaluation, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private CorrectiveMeasureImplementationEvaluation evaluation;

    public CorrectiveMeasureRequest() {
    }

    /**
     * Copy constructor.
     * <p>
     * Responsible agents are reused.
     *
     * @param other The instance to copy
     */
    public CorrectiveMeasureRequest(CorrectiveMeasureRequest other) {
        Objects.requireNonNull(other);
        this.description = other.description;
        this.deadline = other.deadline;
        this.phase = other.phase;
        if (other.responsiblePersons != null) {
            this.responsiblePersons = new HashSet<>(other.responsiblePersons);
        }
        if (other.responsibleOrganizations != null) {
            this.responsibleOrganizations = new HashSet<>(other.responsibleOrganizations);
        }
        this.basedOnEvent = other.basedOnEvent;
        this.basedOnOccurrence = other.basedOnOccurrence;
        if (other.evaluation != null) {
            this.evaluation = new CorrectiveMeasureImplementationEvaluation(other.evaluation);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event getBasedOnEvent() {
        return basedOnEvent;
    }

    public void setBasedOnEvent(Event basedOnEvent) {
        this.basedOnEvent = basedOnEvent;
    }

    public Occurrence getBasedOnOccurrence() {
        return basedOnOccurrence;
    }

    public void setBasedOnOccurrence(Occurrence basedOnOccurrence) {
        this.basedOnOccurrence = basedOnOccurrence;
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

    /**
     * Used in safety issues.
     *
     * @return Corrective measure's deadline
     */
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /**
     * Used in safety issues.
     *
     * @return Phase in which this measure is (e.g. applied, verified).
     */
    public URI getPhase() {
        return phase;
    }

    public void setPhase(URI phase) {
        this.phase = phase;
    }

    public CorrectiveMeasureImplementationEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(CorrectiveMeasureImplementationEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        // First 50 characters of the description
        if (description != null) {
            return "CorrectiveMeasureRequest{" +
                    (description.length() > 50 ? description.substring(0, 50) + "..." : description) + '}';
        }
        return "CorrectiveMeasureRequest{" + uri + "}";
    }
}
