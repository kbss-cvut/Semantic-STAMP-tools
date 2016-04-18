package cz.cvut.kbss.inbas.reporting.service.validation;

import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.util.Constants;

import java.util.Date;
import java.util.Objects;

public class OccurrenceReportValidator extends Validator<OccurrenceReport> {

    public OccurrenceReportValidator() {
    }

    public OccurrenceReportValidator(Validator<? super OccurrenceReport> next) {
        super(next);
    }

    @Override
    public void validateForPersist(OccurrenceReport instance) throws ValidationException {
        Objects.requireNonNull(instance);
        if (instance.getOccurrenceStart() != null) {
            if (instance.getOccurrenceStart().compareTo(new Date()) >= 0) {
                throw new ValidationException("Occurrence start cannot be in the future.");
            }
            if (instance.getOccurrenceEnd() != null &&
                    instance.getOccurrenceStart().compareTo(instance.getOccurrenceEnd()) > 0) {
                throw new ValidationException("Occurrence start cannot be after occurrence end.");
            }
        }
        if (instance.getOccurrence() != null &&
                (instance.getOccurrence().getName() == null || instance.getOccurrence().getName().isEmpty())) {
            throw new ValidationException("Occurrence name cannot be empty.");
        }
        validateArmsIndex(instance);
        super.validateForPersist(instance);
    }

    private void validateArmsIndex(OccurrenceReport report) {
        if (report.getArmsIndex() != null) {
            if (report.getArmsIndex() < Constants.ARMS_INDEX_MIN || report.getArmsIndex() > Constants.ARMS_INDEX_MAX) {
                throw new ValidationException(
                        "ARMS index value " + report.getArmsIndex() + " is not in the valid range " +
                                Constants.ARMS_INDEX_MIN + " - " + Constants.ARMS_INDEX_MAX);
            }
        }
    }

    @Override
    public void validateForUpdate(OccurrenceReport toValidate, OccurrenceReport original) throws ValidationException {
        Objects.requireNonNull(toValidate);
        validateArmsIndex(toValidate);
        super.validateForUpdate(toValidate, original);
    }
}
