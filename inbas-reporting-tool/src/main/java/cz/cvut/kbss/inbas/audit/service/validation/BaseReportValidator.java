package cz.cvut.kbss.inbas.audit.service.validation;

import cz.cvut.kbss.inbas.audit.exception.ValidationException;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.ValidatableReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class BaseReportValidator implements Validator<ValidatableReport> {

    @Autowired
    private Validator<Occurrence> occurrenceValidator;

    @Override
    public void validate(ValidatableReport report) throws ValidationException {
        Objects.requireNonNull(report);

        if (report.getOccurrence() == null || report.getAuthor() == null) {
            throw new ValidationException(
                    "Report is missing one of the required attributes: occurrence, author. " + report);
        }

        if (report.getOccurrenceStart() == null || report.getOccurrenceEnd() == null) {
            throw new ValidationException("Report is missing occurrence start or end time.");
        }
        if (report.getOccurrenceStart().compareTo(report.getOccurrenceEnd()) > 0) {
            throw new ValidationException("Occurrence start time cannot be greater than its end time.");
        }
        final Date now = new Date();
        if (now.compareTo(report.getOccurrenceStart()) < 0) {
            throw new ValidationException("Occurrence time cannot be in the future.");
        }
        if (report.getSeverityAssessment() == null) {
            throw new ValidationException("Report is missing occurrence class.");
        }
        if (report.getOccurrenceCategory() == null) {
            throw new ValidationException("Report is missing occurrence category.");
        }
        if (report.getSummary() == null || report.getSummary().isEmpty()) {
            throw new ValidationException("Report is missing narrative.");
        }
        occurrenceValidator.validate(report.getOccurrence());
    }
}
