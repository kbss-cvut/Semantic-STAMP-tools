package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.IdentificationUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.Set;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.SeverityAssessment)
public class SeverityAssessment implements ReportingStatement, HasOwlKey {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    @OWLDataProperty(iri = Vocabulary.p_severityLevel)
    private Integer level;

    @OWLObjectProperty(iri = Vocabulary.p_hasResource, cascade = CascadeType.ALL)
    private Set<Resource> resources;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void generateKey() {
        if (key != null) {
            return;
        }
        this.key = IdentificationUtils.generateKey();
    }

    @Override
    public String toString() {
        return "SeverityAssessment{" +
                "uri=" + uri +
                ", level=" + level +
                '}';
    }
}
