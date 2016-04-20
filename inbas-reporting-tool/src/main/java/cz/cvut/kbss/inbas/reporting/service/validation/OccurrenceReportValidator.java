package cz.cvut.kbss.inbas.reporting.service.validation;

import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.util.Constants;

import java.util.Objects;

public class OccurrenceReportValidator extends Validator<OccurrenceReport> {

    private final OccurrenceValidator occurrenceValidator = new OccurrenceValidator();

    public OccurrenceReportValidator() {
    }

    public OccurrenceReportValidator(Validator<? super OccurrenceReport> next) {
        super(next);
    }

    @Override
    public void validateForPersist(OccurrenceReport instance) throws ValidationException {
        Objects.requireNonNull(instance);
        if (instance.getOccurrence() != null) {
            occurrenceValidator.validateForPersist(instance.getOccurrence());
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
        if (toValidate.getOccurrence() != null) {
            occurrenceValidator.validateForUpdate(toValidate.getOccurrence(), original.getOccurrence());
        }
        validateArmsIndex(toValidate);
        super.validateForUpdate(toValidate, original);
    }
}
