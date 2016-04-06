package cz.cvut.kbss.inbas.reporting.dto.event;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.cvut.kbss.inbas.reporting.model_new.FactorType;

import java.net.URI;

// Note that every reference has to be defined before its first use
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class FactorDto {

    private URI uri;

    private EventDto event;

    private FactorType type;

    // Reference ID for JSON references
    private Integer referenceId;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public EventDto getEvent() {
        return event;
    }

    public void setEvent(EventDto event) {
        this.event = event;
    }

    public FactorType getType() {
        return type;
    }

    public void setType(FactorType type) {
        this.type = type;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }
}
