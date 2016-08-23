package cz.cvut.kbss.inbas.reporting.model.audit;

import cz.cvut.kbss.inbas.reporting.model.AbstractEntity;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import java.net.URI;

public class AuditFinding extends AbstractEntity {

    @ParticipationConstraints(nonEmpty = true)
    // TODO
    private URI checklist;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_description)
    private String description;

    public URI getChecklist() {
        return checklist;
    }

    public void setChecklist(URI checklist) {
        this.checklist = checklist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AuditFinding{" +
                "checklist=" + checklist +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
