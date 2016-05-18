/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model.qam;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@OWLClass(iri = Vocabulary.Question)
public class Question {
    
    @Id(generated = true)
    protected URI uri;// eccairs entity/attribute
    
    @Types 
    protected Set<URI> types = new HashSet<>();
    
    @OWLObjectProperty(iri = Vocabulary.hasRelatedQuestion, cascade = {CascadeType.PERSIST})
    protected Set<Question> subQuestions = new HashSet<>();

    @OWLObjectProperty(iri = Vocabulary.hasAnswer, cascade = {CascadeType.PERSIST})
    protected Set<Answer> answers = new HashSet<>();// entity instance or attribute value
    
//    @OWLAnnotationProperty(iri = Vocabulary.hasRelationToAnswer)
//    protected URI relationToAnswer;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Set<Question> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(Set<Question> subQuestions) {
        this.subQuestions = subQuestions;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

//    public URI getRelationToAnswer() {
//        return relationToAnswer;
//    }
//
//    public void setRelationToAnswer(URI relationToAnswer) {
//        this.relationToAnswer = relationToAnswer;
//    }

    public Set<URI> getTypes() {
        return types;
    }

    public void setTypes(Set<URI> types) {
        this.types = types;
    }
}
