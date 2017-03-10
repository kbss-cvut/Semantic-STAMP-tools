package cz.cvut.kbss.reporting.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_corrective_measure_implementation_evaluation)
public class CorrectiveMeasureImplementationEvaluation extends AbstractEntity {

    @OWLDataProperty(iri = Vocabulary.s_p_description)
    private String description;

    @Types
    private Set<String> types;

    public CorrectiveMeasureImplementationEvaluation() {
    }

    public CorrectiveMeasureImplementationEvaluation(CorrectiveMeasureImplementationEvaluation other) {
        Objects.requireNonNull(other);
        this.description = other.description;
        this.types = other.types != null ? new HashSet<>(other.types) : null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "CorrectiveMeasureImplementationEvaluation{" +
                "description='" + description + '\'' +
                ", types=" + types +
                "} " + super.toString();
    }
}
