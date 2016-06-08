package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersonTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Person person;

    @Before
    public void setUp() {
        this.person = new Person();
    }

    @Test
    public void newInstanceHasAgentInTypes() {
        assertTrue(person.getTypes().contains(Vocabulary.s_c_Agent));
    }

    @Test
    public void encodePasswordThrowsIllegalStateForNullPassword() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot encode an empty password.");
        person.encodePassword(new StandardPasswordEncoder());
    }

    @Test
    public void generateUriCreatesUriFromFirstNameAndLastName() {
        person.setFirstName("a");
        person.setLastName("b");
        person.generateUri();
        assertEquals(URI.create(Constants.PERSON_BASE_URI + "a+b"), person.getUri());
    }

    @Test
    public void generateUriThrowsIllegalStateForMissingFirstName() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot generate Person URI without first name.");
        person.setLastName("b");
        person.generateUri();
    }

    @Test
    public void generateUriThrowsIllegalStateForMissingLastName() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot generate Person URI without last name.");
        person.setFirstName("a");
        person.generateUri();
    }
}