package cz.cvut.kbss.inbas.audit.services.validation;

import cz.cvut.kbss.inbas.audit.exceptions.InvalidReportException;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;

/**
 * @author ledvima1
 */
public interface ReportValidator {

    /**
     * Validates the specified report, checking its values.
     *
     * @param report The report to validate
     * @throws InvalidReportException When the report is not valid
     */
    void validateReport(OccurrenceReport report) throws InvalidReportException;
}
