package cz.cvut.kbss.inbas.reporting.dto.incursion;

import cz.cvut.kbss.inbas.reporting.model.Organization;

public class AircraftIntruderDto extends RunwayIntruderDto {

    private String registration;

    private String stateOfRegistry;

    private String callSign;

    private Organization operator;

    private String flightNumber;

    private String flightPhase;

    private String operationType;

    private String lastDeparturePoint;

    private String plannedDestination;

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

    public Organization getOperator() {
        return operator;
    }

    public void setOperator(Organization operator) {
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
