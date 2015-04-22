package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;

import java.net.URI;

/**
 * Created by ledvima1 on 20.3.15.
 */
@OWLClass(iri = "http://krizik.felk.cvut.cz/ontologies/inbas/entities#MeasureApplication")
public class MeasureApplication {

    @Id
    private URI uri;

    @OWLObjectProperty(iri = "http://krizik.felk.cvut.cz/ontologies/inbas/attributes#hasAuthor", fetch = FetchType.EAGER)
    private Person author;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "MeasureApplication{" +
                "author=" + author +
                '}';
    }
}
