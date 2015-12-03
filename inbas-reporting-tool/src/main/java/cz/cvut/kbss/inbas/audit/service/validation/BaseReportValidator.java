package cz.cvut.kbss.inbas.audit.service.validation;

import cz.cvut.kbss.inbas.audit.exception.ValidationException;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.ValidatableReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                    "Preliminary report is missing one of the required attributes: occurrence, author. " + report);
        }
        occurrenceValidator.validate(report.getOccurrence());
    }
}
