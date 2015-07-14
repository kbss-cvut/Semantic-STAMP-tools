package cz.cvut.kbss.inbas.audit.rest.dto.model.incursion;

import cz.cvut.kbss.inbas.audit.model.Vehicle;

/**
 * @author ledvima1
 */
public class VehicleIntruder extends RunwayIntruder {

    private String type;

    private String callSign;

    private String organization;

    private String isAtsUnit;

    private String hasRadio;

    private String wasDoing;

    public VehicleIntruder() {
    }

    public VehicleIntruder(Vehicle vehicle) {
        super(vehicle.getUri(), Vehicle.INTRUDER_TYPE);
        this.type = vehicle.getVehicleType();
        this.callSign = vehicle.getCallSign();
        this.isAtsUnit = vehicle.getIsAtsUnit();
        this.hasRadio = vehicle.getHasRadio();
        this.wasDoing = vehicle.getWhatWasDoing();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
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

    @Override
    public String toString() {
        return "VehicleIntruder{" +
                "type='" + type + '\'' +
                ", callSign='" + callSign + '\'' +
                ", organization='" + organization + '\'' +
                "} " + super.toString();
    }
}
