package cz.cvut.kbss.inbas.reporting.dto.event;

import cz.cvut.kbss.inbas.reporting.model.FactorType;

import java.net.URI;

public class FactorDto {

    private URI uri;

    private Integer event;

    private FactorType type;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public FactorType getType() {
        return type;
    }

    public void setType(FactorType type) {
        this.type = type;
    }
}
