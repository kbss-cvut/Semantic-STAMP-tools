package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;

import java.io.Serializable;
import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.Resource)
public class Resource implements Serializable {

    @Id(generated = true)
    private URI uri;

    // TODO Identifier of the resource file
}
