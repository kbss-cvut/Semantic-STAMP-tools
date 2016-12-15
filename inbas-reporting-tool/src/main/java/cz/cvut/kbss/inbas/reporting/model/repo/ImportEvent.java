/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model.repo;

import cz.cvut.kbss.inbas.reporting.model.AbstractEntity;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import java.net.URI;
import java.util.Date;
import java.util.Set;


/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@OWLClass(iri = Vocabulary.s_c_import_event)
public class ImportEvent extends AbstractEntity{
    
    @OWLObjectProperty(iri = Vocabulary.s_p_has_source)
    private RemoteReportRepository importSource;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_target,fetch = FetchType.EAGER)
    private Set<URI> importedDocuments;
    
    @OWLDataProperty(iri = Vocabulary.s_p_has_start_time)
    private Date eventDate;

    public RemoteReportRepository getImportSource() {
        return importSource;
    }

    public void setImportSource(RemoteReportRepository importSource) {
        this.importSource = importSource;
    }

    public Set<URI> getImportedDocuments() {
        return importedDocuments;
    }

    public void setImportedDocuments(Set<URI> importedDocuments) {
        this.importedDocuments = importedDocuments;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
    
}
