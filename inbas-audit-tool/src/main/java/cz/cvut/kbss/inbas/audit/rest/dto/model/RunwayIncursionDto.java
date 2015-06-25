package cz.cvut.kbss.inbas.audit.rest.dto.model;

import cz.cvut.kbss.inbas.audit.model.Aircraft;

/**
 * @author ledvima1
 */
public class RunwayIncursionDto extends EventTypeAssessmentDto {

    private String eventType;

    private String lvp;

    private Aircraft clearedAircraft;

    private IntruderDto intruder;

    public String getLvp() {
        return lvp;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setLvp(String lvp) {
        this.lvp = lvp;
    }

    public Aircraft getClearedAircraft() {
        return clearedAircraft;
    }

    public void setClearedAircraft(Aircraft clearedAircraft) {
        this.clearedAircraft = clearedAircraft;
    }

    public IntruderDto getIntruder() {
        return intruder;
    }

    public void setIntruder(IntruderDto intruder) {
        this.intruder = intruder;
    }

    @Override
    public String toString() {
        return "RunwayIncursionDto{" +
                "clearedAircraft=" + clearedAircraft +
                ", intruder=" + intruder +
                "} " + super.toString();
    }
}
