package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.model.Resource;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.Set;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.SeverityAssessment)
public class SeverityAssessment implements ReportingStatement {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_severityLevel)
    private OccurrenceSeverity severity;

    @OWLObjectProperty(iri = Vocabulary.p_hasResource, cascade = CascadeType.ALL)
    private Set<Resource> resources;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public OccurrenceSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(OccurrenceSeverity severity) {
        this.severity = severity;
    }

    @Override
    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        return "SeverityAssessment{" +
                "uri=" + uri +
                ", severity=" + severity +
                '}';
    }
}
