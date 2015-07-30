package cz.cvut.kbss.inbas.audit.services.impl.validation;

import cz.cvut.kbss.inbas.audit.exceptions.InvalidReportException;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.services.validation.ReportValidator;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author ledvima1
 */
@Service
public class BaseReportValidator implements ReportValidator {

    @Override
    public void validateReport(OccurrenceReport report) throws InvalidReportException {
        Objects.requireNonNull(report);

        if (report.getOccurrenceTime() == null || report.getAuthor() == null || report.getDescription() == null) {
            throw new InvalidReportException(
                    "Occurrence report is missing one of the required attributes: event time, author, description. " + report);
        }
        final Date now = new Date();
        if (now.compareTo(report.getOccurrenceTime()) < 0) {
            throw new InvalidReportException("Occurrence time cannot be in the future.");
        }
    }
}
