package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;

@OWLClass(iri = Vocabulary.Aircraft)
public class Aircraft implements Serializable {

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

    @OWLDataProperty(iri = Vocabulary.p_registration)
    private String registration;

    @OWLDataProperty(iri = Vocabulary.p_stateOfRegistry)
    private String stateOfRegistry;

    @OWLObjectProperty(iri = Vocabulary.p_hasOperator, fetch = FetchType.EAGER)
    private Organization operator;

    public Aircraft() {
    }

    public Aircraft(Aircraft other) {
        this.flightNumber = other.flightNumber;
        this.callSign = other.callSign;
        this.flightPhase = other.flightPhase;
        this.operationType = other.operationType;
        this.lastDeparturePoint = other.lastDeparturePoint;
        this.plannedDestination = other.plannedDestination;
        this.registration = other.registration;
        this.stateOfRegistry = other.stateOfRegistry;
        this.operator = other.operator;
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

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getStateOfRegistry() {
        return stateOfRegistry;
    }

    public void setStateOfRegistry(String stateOfRegistry) {
        this.stateOfRegistry = stateOfRegistry;
    }

    public Organization getOperator() {
        return operator;
    }

    public void setOperator(Organization operator) {
        this.operator = operator;
    }
}
