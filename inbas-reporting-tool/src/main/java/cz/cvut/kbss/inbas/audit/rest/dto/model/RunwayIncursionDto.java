package cz.cvut.kbss.inbas.audit.rest.dto.model;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.EventType;

import java.net.URI;

/**
 * @author ledvima1
 */
public class RunwayIncursionDto extends EventTypeAssessmentDto {

    private URI uri;

    private EventType eventType;

    private String lvp;

    private Aircraft clearedAircraft;

    private IntruderDto intruder;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getLvp() {
        return lvp;
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
                "eventType=" + eventType +
                ", lvp='" + lvp + '\'' +
                ", clearedAircraft=" + clearedAircraft +
                ", intruder=" + intruder +
                "} " + super.toString();
    }
}
