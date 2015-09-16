package cz.cvut.kbss.inbas.audit.model.reports.incursions;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.Intruder)
public class Intruder implements Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLObjectProperty(iri = Vocabulary.p_hasAircraft, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Aircraft aircraft;

    @OWLObjectProperty(iri = Vocabulary.p_hasVehicle, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Vehicle vehicle;

    @OWLObjectProperty(iri = Vocabulary.p_hasPersonIntruder, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PersonIntruder person;

    public Intruder() {
    }

    public Intruder(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public Intruder(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Intruder(PersonIntruder person) {
        this.person = person;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public PersonIntruder getPerson() {
        return person;
    }

    public void setPerson(PersonIntruder person) {
        this.person = person;
    }
}
