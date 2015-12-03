package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;

/**
 * Report that can be validated.
 */
public interface ValidatableReport extends Report {

    Occurrence getOccurrence();

    Person getAuthor();

    String getSummary();
}
