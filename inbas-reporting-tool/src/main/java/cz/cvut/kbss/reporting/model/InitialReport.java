package cz.cvut.kbss.reporting.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import static cz.cvut.kbss.reporting.util.Constants.DESCRIPTION_TO_STRING_THRESHOLD;

@OWLClass(iri = Vocabulary.s_c_initial_report)
public class InitialReport extends AbstractEntity {

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_description)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "InitialReport{" +
                "description='" + (description.length() > DESCRIPTION_TO_STRING_THRESHOLD ?
                                   description.substring(0, DESCRIPTION_TO_STRING_THRESHOLD) + "..." :
                                   description) + '\'' + "}";
    }
}
