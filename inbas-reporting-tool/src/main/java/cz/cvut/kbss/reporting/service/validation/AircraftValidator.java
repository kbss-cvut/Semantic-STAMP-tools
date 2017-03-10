package cz.cvut.kbss.reporting.service.validation;

import cz.cvut.kbss.reporting.exception.ValidationException;
import cz.cvut.kbss.reporting.model.Aircraft;

public class AircraftValidator extends Validator<Aircraft> {

    @Override
    public void validateForPersist(Aircraft instance) throws ValidationException {
        validate(instance);
        super.validateForPersist(instance);
    }

    private void validate(Aircraft aircraft) {
        if (aircraft.getOperator() == null) {
            throw new ValidationException("Missing operator of aircraft " + aircraft + ".");
        }
    }

    @Override
    public void validateForUpdate(Aircraft toValidate, Aircraft original) throws ValidationException {
        validateForPersist(toValidate);
        super.validateForUpdate(toValidate, original);
    }
}
