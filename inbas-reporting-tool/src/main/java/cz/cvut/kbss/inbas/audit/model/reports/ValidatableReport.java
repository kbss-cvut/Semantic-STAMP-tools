package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;

import java.util.Date;

/**
 * Report that can be validated.
 */
public interface ValidatableReport extends Report {

    Occurrence getOccurrence();

    Date getOccurrenceStart();

    Date getOccurrenceEnd();

    Person getAuthor();

    OccurrenceSeverity getSeverityAssessment();

    Short getArmsIndex();

    String getSummary();
}
