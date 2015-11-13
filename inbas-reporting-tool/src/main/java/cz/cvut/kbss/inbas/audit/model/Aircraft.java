package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;

@OWLClass(iri = Vocabulary.Aircraft)
public class Aircraft implements Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_registration)
    private String registration;

    @OWLDataProperty(iri = Vocabulary.p_stateOfRegistry)
    private String stateOfRegistry;

    @OWLObjectProperty(iri = Vocabulary.p_hasOperator, fetch = FetchType.EAGER)
    private Organization operator;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getStateOfRegistry() {
        return stateOfRegistry;
    }

    public void setStateOfRegistry(String stateOfRegistry) {
        this.stateOfRegistry = stateOfRegistry;
    }

    public Organization getOperator() {
        return operator;
    }

    public void setOperator(Organization operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "registration='" + registration + '\'' +
                ", stateOfRegistry='" + stateOfRegistry + '\'' +
                ", operator=" + operator +
                '}';
    }
}
