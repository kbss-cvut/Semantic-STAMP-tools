/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.reporting.model.repo;

import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.reporting.model.AbstractEntity;
import cz.cvut.kbss.reporting.model.Vocabulary;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@OWLClass( iri = Vocabulary.s_c_document_repository)
public class RemoteReportRepository extends AbstractEntity {
    
    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String name;
    
    @OWLObjectProperty(iri = Vocabulary.s_p_inv_dot_has_source, fetch = FetchType.EAGER)
    private Set<ImportEvent> importEvents;
    
    @OWLObjectProperty(iri = Vocabulary.s_p_last_import, fetch = FetchType.EAGER)
    private ImportEvent lastImportEvent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ImportEvent> getImportEvents() {
        return importEvents;
    }

    public void setImportEvents(Set<ImportEvent> importEvents) {
        this.importEvents = importEvents;
    }

    public ImportEvent getLastImportEvent() {
        return lastImportEvent;
    }

    public void setLastImportEvent(ImportEvent lastImportEvent) {
        this.lastImportEvent = lastImportEvent;
    }
    
    public void addImportEvent(ImportEvent importeEvent){
        if(importEvents == null) 
            importEvents = new HashSet<>();
        importEvents.add(importeEvent);
        lastImportEvent = importeEvent;
        importeEvent.setImportSource(this);
    }
    
}
