package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.model.AbstractEntity;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import cz.cvut.kbss.jopa.model.annotations.Types;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_safety_issue)
public class SafetyIssue extends AbstractEntity implements Serializable {

    @ParticipationConstraints(nonEmpty = true)
    @OWLAnnotationProperty(iri = Vocabulary.s_p_label)
    private String name;

    @Types
    private Set<String> types;

    public SafetyIssue() {
        this.types = new HashSet<>(4);
        types.add(Vocabulary.s_c_event_type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "SafetyIssue{" + name + '}';
    }
}
