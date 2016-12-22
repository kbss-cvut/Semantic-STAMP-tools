package cz.cvut.kbss.reporting.service.validation;

import cz.cvut.kbss.reporting.exception.ValidationException;
import cz.cvut.kbss.reporting.model.audit.AuditReport;

import java.util.Objects;

public class AuditReportValidator extends Validator<AuditReport> {

    private final AuditValidator auditValidator = new AuditValidator();

    public AuditReportValidator() {
    }

    public AuditReportValidator(Validator<? super AuditReport> next) {
        super(next);
    }

    @Override
    public void validateForPersist(AuditReport instance) throws ValidationException {
        Objects.requireNonNull(instance);
        if (instance.getAudit() != null) {
            auditValidator.validateForPersist(instance.getAudit());
        }
        super.validateForPersist(instance);
    }

    @Override
    public void validateForUpdate(AuditReport toValidate, AuditReport original) throws ValidationException {
        Objects.requireNonNull(toValidate);
        Objects.requireNonNull(original);
        if (toValidate.getAudit() != null) {
            auditValidator.validateForUpdate(toValidate.getAudit(), original.getAudit());
        }
        super.validateForUpdate(toValidate, original);
    }
}
