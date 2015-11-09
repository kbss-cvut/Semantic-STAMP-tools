package cz.cvut.kbss.inbas.audit.service.validation;

import cz.cvut.kbss.inbas.audit.exception.ValidationException;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class OccurrenceValidator implements Validator<Occurrence> {

    @Override
    public void validate(Occurrence instance) throws ValidationException {
        Objects.requireNonNull(instance);

        if (instance.getStartTime() == null || instance.getEndTime() == null) {
            throw new ValidationException("Occurrence is missing start or end time.");
        }
        if (instance.getStartTime().compareTo(instance.getEndTime()) > 0) {
            throw new ValidationException("Occurrence start time cannot be greater than its end time.");
        }
        final Date now = new Date();
        if (now.compareTo(instance.getStartTime()) < 0) {
            throw new ValidationException("Occurrence time cannot be in the future.");
        }
    }
}
