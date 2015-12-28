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

        if (instance.getName() == null || instance.getName().isEmpty()) {
            throw new ValidationException("Occurrence name cannot be empty.");
        }
    }
}
