package cz.cvut.kbss.inbas.reporting.service.validation;

import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;

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
            if (instance.getOccurrence() != null &&
                    (instance.getOccurrence().getName() == null || instance.getOccurrence().getName().isEmpty())) {
                throw new ValidationException("Occurrence name cannot be empty.");
            }
        }
        super.validateForPersist(instance);
    }

    @Override
    public void validateForUpdate(OccurrenceReport toValidate, OccurrenceReport original) throws ValidationException {
        super.validateForUpdate(toValidate, original);
    }
}
