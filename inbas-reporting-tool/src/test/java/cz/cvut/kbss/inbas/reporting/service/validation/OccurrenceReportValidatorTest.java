package cz.cvut.kbss.inbas.reporting.service.validation;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model_new.Vocabulary;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ValidatorFactory.class})
public class OccurrenceReportValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private OccurrenceReportValidator validator;

    @Test
    public void validReportPassesPersistValidation() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        validator.validateForPersist(report);
    }

    @Test
    public void validReportPassesUpdateValidation() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setKey(IdentificationUtils.generateKey());
        report.setUri(URI.create(Vocabulary.OccurrenceReport + "#instance"));
        report.getAuthor().generateUri();
        final OccurrenceReport copy = new OccurrenceReport(report);
        copy.setKey(report.getKey());
        copy.setUri(report.getUri());
        copy.setAuthor(report.getAuthor());
        copy.setDateCreated(report.getDateCreated());
        copy.setRevision(report.getRevision());
        validator.validateForUpdate(copy, report);
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

    @Test
    public void negativeArmsIndexIsInvalid() {
        thrown.expect(ValidationException.class);
        final short armsValue = Short.MIN_VALUE;
        thrown.expectMessage(
                "ARMS index value " + armsValue + " is not in the valid range " + Constants.ARMS_INDEX_MIN + " - " +
                        Constants.ARMS_INDEX_MAX);
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setArmsIndex(armsValue);
        validator.validateForPersist(report);
    }

    @Test
    public void tooLargeArmsIndexIsInvalid() {
        thrown.expect(ValidationException.class);
        final short armsValue = Short.MAX_VALUE;
        assertTrue(armsValue > Constants.ARMS_INDEX_MAX);
        thrown.expectMessage(
                "ARMS index value " + armsValue + " is not in the valid range " + Constants.ARMS_INDEX_MIN + " - " +
                        Constants.ARMS_INDEX_MAX);
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setArmsIndex((short) 10);
        final OccurrenceReport copy = new OccurrenceReport(report);
        copy.setArmsIndex(armsValue);
        validator.validateForUpdate(copy, report);
    }

    @Test
    public void occurrenceValidatorCallsNextValidatorInChain() {
        thrown.expect(ValidationException.class);
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setKey(IdentificationUtils.generateKey());
        final OccurrenceReport copy = new OccurrenceReport(report);
        copy.setKey(IdentificationUtils.generateKey()); // The key will be different
        validator.validateForUpdate(copy, report);
    }
}