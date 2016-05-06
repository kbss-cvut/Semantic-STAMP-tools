/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model.qam;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;
import java.net.URI;
import java.util.Set;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@OWLClass(iri = Vocabulary.Answer)
public class Answer {
    @Types
    private Set<String> types;
    
    @Id
    protected URI uri;
    
    @OWLObjectProperty(iri = Vocabulary.hasPart)
    protected Set<Answer> answerParts;
    
    @OWLDataProperty(iri = Vocabulary.hasValue)
    protected String textValue;
    
    @OWLObjectProperty(iri = Vocabulary.hasValue)
    protected URIWrap codValue;
}
