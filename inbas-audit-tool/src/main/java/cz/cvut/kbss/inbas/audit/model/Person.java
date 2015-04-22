package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Properties;

import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * Created by ledvima1 on 20.3.15.
 */
@OWLClass(iri = "http://xmlns.com/foaf/0.1/Person")
public class Person {

    @Id
    private URI uri;

    @OWLDataProperty(iri = "http://xmlns.com/foaf/0.1/firstName")
    private String firstName;

    @OWLDataProperty(iri = "http://xmlns.com/foaf/0.1/lastName")
    private String lastName;

    @Properties
    private Map<String, Set<String>> properties;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<String, Set<String>> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Set<String>> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Person{" +
                "uri=" + uri +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
