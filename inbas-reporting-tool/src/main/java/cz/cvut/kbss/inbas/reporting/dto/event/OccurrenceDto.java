package cz.cvut.kbss.inbas.reporting.dto.event;

import cz.cvut.kbss.inbas.reporting.model_new.Occurrence;

import java.util.Objects;

public class OccurrenceDto extends EventDto {

    private String key;

    private String name;

    public OccurrenceDto() {
    }

    public OccurrenceDto(Occurrence occurrence) {
        Objects.requireNonNull(occurrence);
        this.key = occurrence.getKey();
        this.name = occurrence.getName();
        setUri(occurrence.getUri());
        setType(occurrence.getType());
        setTypes(occurrence.getTypes());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
