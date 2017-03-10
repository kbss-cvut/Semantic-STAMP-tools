package cz.cvut.kbss.reporting.service.validation;

import cz.cvut.kbss.reporting.exception.ValidationException;
import cz.cvut.kbss.reporting.model.audit.Audit;

import java.util.Date;

public class AuditValidator extends Validator<Audit> {

    @Override
    public void validateForPersist(Audit instance) throws ValidationException {
        validateAudit(instance);
    }

    private void validateAudit(Audit instance) {
        if (instance.getStartDate() != null) {
            if (instance.getStartDate().after(new Date())) {
                throw new ValidationException("Audit start cannot be in the future.");
            }
            if (instance.getStartDate().compareTo(instance.getEndDate()) > 0) {
                throw new ValidationException("Audit start cannot be after its end.");
            }
        }
        if (instance.getName() == null || instance.getName().isEmpty()) {
            throw new ValidationException("Audit name is missing.");
        }
        if (instance.getAuditee() == null) {
            throw new ValidationException("Audited organization is missing.");
        }
    }

    @Override
    public void validateForUpdate(Audit toValidate, Audit original) throws ValidationException {
        validateAudit(toValidate);
    }
}
