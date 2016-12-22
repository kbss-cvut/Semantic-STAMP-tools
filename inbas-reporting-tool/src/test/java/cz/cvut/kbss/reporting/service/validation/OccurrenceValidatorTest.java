package cz.cvut.kbss.reporting.service.validation;

import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.exception.ValidationException;
import cz.cvut.kbss.reporting.model.Event;
import cz.cvut.kbss.reporting.model.Occurrence;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

public class OccurrenceValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private OccurrenceValidator validator = new OccurrenceValidator();

    @Test
    public void eventInFactorGraphWithoutStartTimeIsInvalidForPersist() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrenceWithDescendantEvents();
        final Event evt = occurrence.getChildren().iterator().next();
        evt.setStartTime(null);
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Event " + evt + " is missing the required start time value.");
        validator.validateForPersist(occurrence);
    }

    @Test
    public void eventInFactorGraphWithoutEndTimeIsInvalidForUpdate() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrenceWithDescendantEvents();
        final Event evt = occurrence.getChildren().iterator().next();
        evt.setEndTime(null);
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Event " + evt + " is missing the required end time value.");
        validator.validateForUpdate(occurrence, occurrence);
    }

    @Test
    public void eventWithEndTimeBeforeStartTimeIsInvalid() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrenceWithDescendantEvents();
        final Event evt = occurrence.getChildren().iterator().next();
        evt.setEndTime(new Date(evt.getStartTime().getTime() - 1000));
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Event start cannot be after its end.");
        validator.validateForPersist(occurrence);
    }
}