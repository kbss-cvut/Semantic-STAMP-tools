/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model.qam;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@OWLClass(iri = Vocabulary.Answer)
public class Answer {
    @Types
    private Set<String> types = new HashSet<>();
    
    @Id(generated = true)
    protected URI uri;
    
    @OWLDataProperty(iri = Vocabulary.hasValue)
    protected String textValue;
    
    @OWLObjectProperty(iri = Vocabulary.hasURIValue)
    protected URI codValue;

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }
    
    public void addType(String type){
        types.add(type);
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

    public URI getCodValue() {
        return codValue;
    }

    public void setCodValue(URI codValue) {
        this.codValue = codValue;
    }
    
}
