package cz.cvut.kbss.inbas.reporting.service.validation;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ValidatorFactory.class})
public class OccurrenceReportValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private OccurrenceReportValidator validator;

    @Test
    public void validReportPassesValidation() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        validator.validateForPersist(report);
    }

    @Test
    public void reportWithFutureOccurrenceStartIsInvalid() {
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Occurrence start cannot be in the future.");
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setOccurrenceStart(new Date(System.currentTimeMillis() + 10000));
        validator.validateForPersist(report);
    }

    @Test
    public void reportWithOccurrenceEndBeforeOccurrenceStartIsInvalid() {
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Occurrence start cannot be after occurrence end.");
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setOccurrenceEnd(new Date(report.getOccurrenceStart().getTime() - 10000));
        validator.validateForPersist(report);
    }

    @Test
    public void emptyOccurrenceNameIsInvalid() {
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Occurrence name cannot be empty.");
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.getOccurrence().setName("");
        validator.validateForPersist(report);
    }
}