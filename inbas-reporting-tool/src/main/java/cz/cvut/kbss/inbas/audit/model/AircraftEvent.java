package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;

@OWLClass(iri = Vocabulary.AircraftEvent)
public class AircraftEvent implements Serializable {

    public static final String INTRUDER_TYPE = "aircraft";

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_flightNumber)
    private String flightNumber;

    @OWLDataProperty(iri = Vocabulary.p_callSign)
    private String callSign;

    @OWLDataProperty(iri = Vocabulary.p_flightPhase)
    private String flightPhase;

    @OWLDataProperty(iri = Vocabulary.p_operationType)
    private String operationType;

    @OWLDataProperty(iri = Vocabulary.p_lastDeparturePoint)
    private String lastDeparturePoint;

    @OWLDataProperty(iri = Vocabulary.p_plannedDestination)
    private String plannedDestination;

    @OWLObjectProperty(iri = Vocabulary.p_hasAircraft, fetch = FetchType.EAGER)
    private Aircraft aircraft;

    public AircraftEvent() {
    }

    public AircraftEvent(AircraftEvent other) {
        this.flightNumber = other.flightNumber;
        this.callSign = other.callSign;
        this.flightPhase = other.flightPhase;
        this.operationType = other.operationType;
        this.lastDeparturePoint = other.lastDeparturePoint;
        this.plannedDestination = other.plannedDestination;
        this.aircraft = other.aircraft;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public String getFlightPhase() {
        return flightPhase;
    }

    public void setFlightPhase(String flightPhase) {
        this.flightPhase = flightPhase;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getLastDeparturePoint() {
        return lastDeparturePoint;
    }

    public void setLastDeparturePoint(String lastDeparturePoint) {
        this.lastDeparturePoint = lastDeparturePoint;
    }

    public String getPlannedDestination() {
        return plannedDestination;
    }

    public void setPlannedDestination(String plannedDestination) {
        this.plannedDestination = plannedDestination;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }
}
