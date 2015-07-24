package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;

import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.EventType)
public class EventType {

    @Id
    private URI id;

    @OWLDataProperty(iri = "http://www.w3.org/2000/01/rdf-schema#label")
    private String name;

    @OWLDataProperty(iri = Vocabulary.p_dtoClass)
    private String dtoClass;

    public EventType() {
    }

    public EventType(URI id) {
        this.id = id;
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

    public String getDtoClass() {
        return dtoClass;
    }

    public void setDtoClass(String dtoClass) {
        this.dtoClass = dtoClass;
    }

    @Override
    public String toString() {
        return "EventType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
