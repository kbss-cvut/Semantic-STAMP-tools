/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model.qam;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;
import java.net.URI;
import java.util.Set;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@OWLClass(iri = Vocabulary.Question)
public class Question {
    
    
    @Id
    protected URI uri;// eccairs entity/attribute

    @OWLObjectProperty(iri = Vocabulary.hasAnswer)
    protected Answer answer;// entity instance or attribute value
    
    @OWLAnnotationProperty(iri = Vocabulary.hasRelationToAnswer)
    protected URI relationToAnswer;
    
}
