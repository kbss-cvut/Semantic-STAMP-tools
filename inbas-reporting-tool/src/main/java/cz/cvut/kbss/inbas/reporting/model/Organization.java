package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;

import java.io.Serializable;
import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.Organization)
public class Organization implements HasDerivableUri, Serializable {

    @Id
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_organizationCode)
    private String code;

    @OWLDataProperty(iri = Vocabulary.p_label)
    private String name;

    public Organization() {
    }

    public Organization(String name) {
        this.name = name;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void generateUri() {
        if (uri != null) {
            return;
        }
        if (code != null) {
            this.uri = URI.create(Constants.ORGANIZATION_BASE_URI + code);
        } else if (name != null) {
            this.uri = URI.create(Constants.ORGANIZATION_BASE_URI + name.replace(' ', '-'));
        } else {
            throw new IllegalStateException("Cannot generate URI of Organization. It is missing both code and name.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        return !(uri != null ? !uri.equals(that.uri) : that.uri != null);

    }

    @Override
    public int hashCode() {
        return uri != null ? uri.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name + " - " + code + "(" + uri + ")";
    }
}
