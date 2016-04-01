package cz.cvut.kbss.inbas.reporting.service.validation;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class OccurrenceReportValidatorTest extends BaseServiceTestRunner {

    @Autowired
    private OccurrenceReportValidator validator;

    @Test
    public void validReportPassesValidation() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        validator.validate(report);
    }

    @Test(expected = ValidationException.class)
    public void reportWithFutureOccurrenceStartIsInvalid() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setOccurrenceStart(new Date(System.currentTimeMillis() + 10000));
        validator.validate(report);
    }

    @Test(expected = ValidationException.class)
    public void reportWithOccurrenceEndBeforeOccurrenceStartIsInvalid() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setOccurrenceEnd(new Date(report.getOccurrenceStart().getTime() - 10000));
        validator.validate(report);
    }
}