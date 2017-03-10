package cz.cvut.kbss.reporting.model;

import cz.cvut.kbss.jopa.model.annotations.*;

import java.util.HashSet;
import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_Aircraft)
public class Aircraft extends AbstractEntity {

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_is_operated_by, fetch = FetchType.EAGER)
    private Organization operator;

    @Types
    private Set<String> types;

    public Aircraft() {
    }

    public Aircraft(Aircraft other) {
        this.operator = other.operator;
        this.types = other.types != null ? new HashSet<>(other.types) : null;
    }

    public Organization getOperator() {
        return operator;
    }

    public void setOperator(Organization operator) {
        this.operator = operator;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    /**
     * Adds {@link Vocabulary#s_c_operator_organization} to the types of this aircraft's operator.
     */
    public void addOperatorOrganizationType() {
        operator.getTypes().add(Vocabulary.s_c_operator_organization);
    }

    @Override
    public String toString() {
        return "Aircraft{(" + types +
                "), operator=" + operator +
                "}";
    }
}
