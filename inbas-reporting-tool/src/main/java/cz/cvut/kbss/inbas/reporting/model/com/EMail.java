/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model.com;

//import cz.cvut.kbss.datatools.mail.model.Message;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@OWLClass(iri = Vocabulary.Email)
public class EMail {
    
    @Id(generated = true)
    protected String uri;
    
//    @Transient
//    protected Message message;
    
    @OWLDataProperty(iri = Vocabulary.hasId)
    protected String id;
    
    @OWLObjectProperty(iri = Vocabulary.hasPart)
    protected Set<URI> reports = new HashSet<URI>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//
//    public Message getMessage() {
//        return message;
//    }
//
//    public void setMessage(Message message) {
//        this.message = message;
//    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Set<URI> getReports() {
        return reports;
    }

    public void setReports(Set<URI> reports) {
        this.reports = reports;
    }
    
    
    
}
