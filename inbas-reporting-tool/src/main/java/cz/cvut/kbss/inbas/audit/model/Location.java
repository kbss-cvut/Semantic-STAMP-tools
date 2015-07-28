package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;

import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.Location)
public class Location {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_simpleLocation)
    private String location;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Location=" + location;
    }
}
