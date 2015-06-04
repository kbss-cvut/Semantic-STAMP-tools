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
    private URI uri;

    @OWLDataProperty(iri = "http://www.w3.org/2000/01/rdf-schema#label")
    private String name;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EventType{" +
                "uri=" + uri +
                ", name='" + name + '\'' +
                '}';
    }
}
