package cz.cvut.kbss.inbas.audit.rest.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.URI;
import java.util.Map;

/**
 * @author ledvima1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntruderDto {

    private URI uri;

    private String intruderType;

    // Aircraft

    private String registration;

    private String stateOfRegistry;

    private String callSign;

    private String operator;

    private String flightNumber;

    private String flightPhase;

    private String operationType;

    private String lastDeparturePoint;

    private String plannedDestination;

    // Vehicle

    private String type;

    private String organization;

    private String isAtsUnit;

    private String hasRadio;

    private String wasDoing;

    // PersonIntruder

    private String personCategory;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getIntruderType() {
        return intruderType;
    }

    public void setIntruderType(String intruderType) {
        this.intruderType = intruderType;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getIsAtsUnit() {
        return isAtsUnit;
    }

    public void setIsAtsUnit(String isAtsUnit) {
        this.isAtsUnit = isAtsUnit;
    }

    public String getHasRadio() {
        return hasRadio;
    }

    public void setHasRadio(String hasRadio) {
        this.hasRadio = hasRadio;
    }

    public String getWasDoing() {
        return wasDoing;
    }

    public void setWasDoing(String wasDoing) {
        this.wasDoing = wasDoing;
    }

    public String getPersonCategory() {
        return personCategory;
    }

    public void setPersonCategory(String personCategory) {
        this.personCategory = personCategory;
    }
}
