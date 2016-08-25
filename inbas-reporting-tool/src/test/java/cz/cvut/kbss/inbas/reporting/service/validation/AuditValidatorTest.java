package cz.cvut.kbss.inbas.reporting.service.validation;

import cz.cvut.kbss.inbas.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model.audit.Audit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

public class AuditValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AuditValidator validator = new AuditValidator();

    @Test
    public void auditWithBeginningAfterEndIsInvalid() {
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Audit start cannot be after its end.");
        final Audit a = AuditReportGenerator.generateAudit();
        a.setEndDate(new Date(System.currentTimeMillis() - 10000));
        a.setStartDate(new Date());
        validator.validateForPersist(a);
    }

    @Test
    public void auditWithBeginningInTheFutureIsInvalid() {
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Audit start cannot be in the future.");
        final Audit a = AuditReportGenerator.generateAudit();
        a.setStartDate(new Date(System.currentTimeMillis() + 10000));
        validator.validateForPersist(a);
    }

    @Test
    public void auditWithoutNameIsInvalid() {
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Audit name is missing.");
        final Audit a = AuditReportGenerator.generateAudit();
        a.setName(null);
        validator.validateForUpdate(a, null);
    }

    @Test
    public void auditWithoutAuditeeIsInvalid() {
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Audited organization is missing.");
        final Audit a = AuditReportGenerator.generateAudit();
        a.setAuditee(null);
        validator.validateForPersist(a);
    }

    @Test
    public void validAuditPassesValidation() {
        final Audit a = AuditReportGenerator.generateAudit();
        validator.validateForPersist(a);
    }
}