package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;

import java.io.Serializable;
import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.Location)
public class Location implements HasDerivableUri, Serializable {

    private static final String BASE = "http://onto.fel.cvut.cz/ontologies/aviation-safety/";

    @Id
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_label)
    private String name;

    public Location() {
    }

    public Location(String name) {
        this.name = name;
    }

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
    public void generateUri() {
        this.uri = URI.create(BASE + name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (uri != null ? !uri.equals(location.uri) : location.uri != null) return false;
        return !(name != null ? !name.equals(location.name) : location.name != null);

    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location=" + uri;
    }
}
