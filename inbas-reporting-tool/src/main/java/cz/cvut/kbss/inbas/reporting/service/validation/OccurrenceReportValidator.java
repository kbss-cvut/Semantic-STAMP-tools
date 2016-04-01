package cz.cvut.kbss.inbas.reporting.service.validation;

import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class OccurrenceReportValidator implements Validator<OccurrenceReport> {

    @Override
    public void validate(OccurrenceReport instance) throws ValidationException {
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
    }
}
