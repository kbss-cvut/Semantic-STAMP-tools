package cz.cvut.kbss.inbas.reporting.dto.incursion;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cvut.kbss.inbas.reporting.model.Aircraft;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.PersonIntruder;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.Vehicle;

import java.net.URI;

/**
 * @author ledvima1
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "intruderType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AircraftIntruderDto.class, name = Aircraft.INTRUDER_TYPE),
        @JsonSubTypes.Type(value = VehicleIntruderDto.class, name = Vehicle.INTRUDER_TYPE),
        @JsonSubTypes.Type(value = PersonIntruderDto.class, name = PersonIntruder.INTRUDER_TYPE)
})
public abstract class RunwayIntruderDto {

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
