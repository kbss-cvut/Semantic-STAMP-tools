package cz.cvut.kbss.reporting.service.validation;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.exception.ValidationException;
import cz.cvut.kbss.reporting.model.Aircraft;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class AircraftValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AircraftValidator validator = new AircraftValidator();

    @Test
    public void aircraftWithoutOperatorIsInvalidForPersist() {
        final Aircraft aircraft = new Aircraft();
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Missing operator of aircraft " + aircraft + ".");
        validator.validateForPersist(aircraft);
    }

    @Test
    public void aircraftWithoutOperatorIsInvalidForUpdate() {
        final Aircraft aircraft = new Aircraft();
        aircraft.setUri(Generator.generateUri());
        final Aircraft update = new Aircraft(aircraft);
        update.setUri(aircraft.getUri());
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Missing operator of aircraft " + update + ".");
        validator.validateForUpdate(update, aircraft);
    }
}