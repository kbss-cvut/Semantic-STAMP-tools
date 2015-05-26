package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;

import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.EventType)
public class EventType {

    @Id
    private URI uri;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "EventType{" +
                "uri=" + uri +
                '}';
    }
}
