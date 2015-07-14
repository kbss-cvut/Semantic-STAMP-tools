package cz.cvut.kbss.inbas.audit.rest.dto.model.incursion;

import cz.cvut.kbss.inbas.audit.model.Aircraft;

import java.net.URI;

/**
 * @author ledvima1
 */
public class AircraftIntruder extends RunwayIntruder {

    private String registration;

    private String stateOfRegistry;

    private String callSign;

    private String operator;

    private String flightNumber;

    private String flightPhase;

    private String operationType;

    private String lastDeparturePoint;

    private String plannedDestination;

    public AircraftIntruder() {
    }

    public AircraftIntruder(Aircraft aircraft) {
        super(aircraft.getUri(), Aircraft.INTRUDER_TYPE);
        this.registration = aircraft.getRegistration();
        this.stateOfRegistry = aircraft.getStateOfRegistry();
        this.callSign = aircraft.getCallSign();
        this.operator = aircraft.getOperator() != null ? aircraft.getOperator().getName() : null;
        this.flightNumber = aircraft.getFlightNumber();
        this.flightPhase = aircraft.getFlightPhase();
        this.operationType = aircraft.getOperationType();
        this.lastDeparturePoint = aircraft.getLastDeparturePoint();
        this.plannedDestination = aircraft.getPlannedDestination();
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

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
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

    @Override
    public String toString() {
        return "AircraftIntruder{" +
                "flightNumber='" + flightNumber + '\'' +
                ", callSign='" + callSign + '\'' +
                ", operator='" + operator + '\'' +
                "} " + super.toString();
    }
}
