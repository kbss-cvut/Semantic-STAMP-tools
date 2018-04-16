package cz.cvut.kbss.reporting.model.location;

import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import cz.cvut.kbss.reporting.model.Vocabulary;

import java.util.Objects;

@OWLClass(iri = Vocabulary.s_c_named_location)
public class NamedLocation extends Location {

    @OWLAnnotationProperty(iri = Vocabulary.s_p_label)
    @ParticipationConstraints(nonEmpty = true)
    private String name;

    public NamedLocation() {
    }

    public NamedLocation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Location copy() {
        return new NamedLocation(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedLocation that = (NamedLocation) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "NamedLocation{" +
                "name='" + name + '\'' +
                '}';
    }
}
