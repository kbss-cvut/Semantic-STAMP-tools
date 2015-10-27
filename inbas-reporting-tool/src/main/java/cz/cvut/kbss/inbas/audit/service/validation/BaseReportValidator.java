package cz.cvut.kbss.inbas.audit.service.validation;

import cz.cvut.kbss.inbas.audit.exception.InvalidReportException;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
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

        if (report.getOccurrenceTime() == null || report.getAuthor() == null) {
            throw new InvalidReportException(
                    "Occurrence report is missing one of the required attributes: occurrence time, author. " + report);
        }
        final Date now = new Date();
        if (now.compareTo(report.getOccurrenceTime()) < 0) {
            throw new InvalidReportException("Occurrence time cannot be in the future.");
        }
    }
}
