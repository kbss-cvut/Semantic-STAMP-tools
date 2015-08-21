package cz.cvut.kbss.inbas.audit.rest.dto.model.incursion;

/**
 * @author ledvima1
 */
public class VehicleIntruder extends RunwayIntruder {

    private String vehicleType;

    private String callSign;

    private String organization;

    private String isAtsUnit;

    private String hasRadio;

    private String wasDoing;

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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
                "vehicleType='" + vehicleType + '\'' +
                ", callSign='" + callSign + '\'' +
                ", organization='" + organization + '\'' +
                "} " + super.toString();
    }
}
