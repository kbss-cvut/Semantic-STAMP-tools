/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model.qam;

import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@OWLClass(iri = Vocabulary.Answer)
public class Answer {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.hasValue)
    private String textValue;

    @OWLObjectProperty(iri = Vocabulary.hasURIValue)
    private URI codeValue;

    @Types
    private Set<String> types = new HashSet<>();

    public Answer() {
    }

    public Answer(Answer other) {
        this.textValue = other.textValue;
        this.codeValue = other.codeValue;
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

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public URI getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(URI codeValue) {
        this.codeValue = codeValue;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public void addType(String type) {
        types.add(type);
    }

    @Override
    public String toString() {
        if (textValue != null && codeValue != null) {
            return "value=" + textValue + ", code=" + codeValue;
        } else if (textValue == null) {
            return "code=" + codeValue;
        } else {
            return "text=" + textValue;
        }
    }
}
