package cz.cvut.kbss.inbas.audit.service.validation;


import cz.cvut.kbss.inbas.audit.exception.ValidationException;

public interface Validator<T> {

    /**
     * Verifies validity of the specified instance.
     *
     * @param instance Instance to validate
     * @throws ValidationException If the validation fails
     */
    void validate(T instance) throws ValidationException;
}
