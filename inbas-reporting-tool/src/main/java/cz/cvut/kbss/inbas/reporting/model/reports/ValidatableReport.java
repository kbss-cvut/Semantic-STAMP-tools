package cz.cvut.kbss.inbas.reporting.model.reports;

import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.Person;

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

    String getSummary();
}
