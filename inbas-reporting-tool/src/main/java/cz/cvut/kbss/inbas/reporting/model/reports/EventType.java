package cz.cvut.kbss.inbas.reporting.model.reports;

import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import java.io.Serializable;
import java.net.URI;

@OWLClass(iri = Vocabulary.EventType)
public class EventType implements Serializable {

    @Id
    private URI id;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_label)
    private String name;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String description;

    @OWLDataProperty(iri = Vocabulary.p_eventType)
    private String type;

    public EventType() {
    }

    public EventType(URI id) {
        this.id = id;
    }

    public EventType(URI id, String name) {
        this.id = id;
        this.name = name;
    }

    public URI getId() {
        return id;
    }

    public void setId(URI id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventType eventType = (EventType) o;

        if (id != null ? !id.equals(eventType.id) : eventType.id != null) return false;
        return !(name != null ? !name.equals(eventType.name) : eventType.name != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
