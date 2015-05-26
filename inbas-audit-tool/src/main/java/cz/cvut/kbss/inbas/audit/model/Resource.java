package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;

import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.Resource)
public class Resource {

    @Id(generated = true)
    private URI uri;

    // TODO Identifier of the resource file
}
