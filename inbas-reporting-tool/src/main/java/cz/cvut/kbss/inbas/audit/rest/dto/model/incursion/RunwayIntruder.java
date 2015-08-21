package cz.cvut.kbss.inbas.audit.rest.dto.model.incursion;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Vehicle;

import java.net.URI;

/**
 * @author ledvima1
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "intruderType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AircraftIntruder.class, name = Aircraft.INTRUDER_TYPE),
        @JsonSubTypes.Type(value = VehicleIntruder.class, name = Vehicle.INTRUDER_TYPE),
        @JsonSubTypes.Type(value = PersonIntruder.class, name = cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder.INTRUDER_TYPE)
})
public abstract class RunwayIntruder {

    private URI uri;

    private String intruderType;

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
}
