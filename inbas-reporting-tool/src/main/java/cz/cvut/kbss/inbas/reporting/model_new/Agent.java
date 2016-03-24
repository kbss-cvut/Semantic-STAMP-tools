package cz.cvut.kbss.inbas.reporting.model_new;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;

import java.io.Serializable;
import java.net.URI;

@OWLClass(iri = Vocabulary.Agent)
public class Agent implements Serializable {

    @Id
    private URI uri;
}
