package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.IdentificationUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.Set;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.CorrectiveMeasure)
public class CorrectiveMeasure implements ReportingStatement, HasOwlKey {

    @Id(generated = true)
    URI uri;

    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String description;

    @OWLObjectProperty(iri = Vocabulary.p_hasResource, cascade = CascadeType.ALL)
    private Set<Resource> resources;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void generateKey() {
        if (key != null) {
            return;
        }
        this.key = IdentificationUtils.generateKey(uri);
    }

    @Override
    public String toString() {
        return "CorrectiveMeasure{" +
                "uri=" + uri +
                ", description='" + description + '\'' +
                '}';
    }
}
