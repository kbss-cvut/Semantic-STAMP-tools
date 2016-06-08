/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model.qam;

import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@OWLClass(iri = Vocabulary.Question)
public class Question implements Serializable {

    @Id(generated = true)
    private URI uri;

    // eccairs entity/attribute
    @Types
    private Set<String> types = new HashSet<>();

    @OWLObjectProperty(iri = Vocabulary.hasRelatedQuestion, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<Question> subQuestions = new HashSet<>();

    @OWLObjectProperty(iri = Vocabulary.hasAnswer, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<Answer> answers = new HashSet<>();// entity instance or attribute value

    public Question() {
    }

    public Question(Question other) {
        if (other.subQuestions != null) {
            this.subQuestions = other.subQuestions.stream().map(Question::new).collect(Collectors.toSet());
        }
        if (other.answers != null) {
            this.answers = other.answers.stream().map(Answer::new).collect(Collectors.toSet());
        }
        if (other.types != null) {
            this.types.addAll(other.types);
        }
    }

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

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Question (" + types + "){" +
                "answers=" + answers +
                ", subQuestions=" + subQuestions +
                '}';
    }
}
