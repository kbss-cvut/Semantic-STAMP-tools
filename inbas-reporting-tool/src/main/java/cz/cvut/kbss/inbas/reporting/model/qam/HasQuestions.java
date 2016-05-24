package cz.cvut.kbss.inbas.reporting.model.qam;

import java.util.Set;

/**
 * Marker interface for classes of the QAM which can have sub questions.
 */
public interface HasQuestions {

    Set<Question> getSubQuestions();

    void setSubQuestions(Set<Question> subQuestions);
}
