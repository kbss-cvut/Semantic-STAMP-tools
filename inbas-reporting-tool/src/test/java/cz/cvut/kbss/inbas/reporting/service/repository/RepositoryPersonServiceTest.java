package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.exception.UsernameExistsException;
import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.reporting.service.PersonService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertTrue;

public class RepositoryPersonServiceTest extends BaseServiceTestRunner {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void persistEncodesPersonPassword() {
        final Person p = Generator.getPerson();
        personService.persist(p);

        final Person result = personService.find(p.getUri());
        assertTrue(passwordEncoder.matches(Generator.PASSWORD, result.getPassword()));
    }

    @Test
    public void persistThrowsUsernameExistsForUserWithDuplicateUsername() {
        final Person p = Generator.getPerson();
        personService.persist(p);
        final Person duplicate = new Person();
        duplicate.setUsername(p.getUsername());
        duplicate.setFirstName("duplicate");
        duplicate.setLastName("duplicated");
        duplicate.setPassword(Generator.PASSWORD);
        thrown.expect(UsernameExistsException.class);
        thrown.expectMessage("Username " + p.getUsername() + " already exists.");
        personService.persist(duplicate);
    }

    @Test
    public void persistThrowsValidationExceptionForInstanceWithoutPassword() {
        final Person p = Generator.getPerson();
        p.setPassword("");
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Cannot encode an empty password.");
        personService.persist(p);
    }

    @Test
    public void updateEncodesNewPassword() {
        final Person p = Generator.getPerson();
        personService.persist(p);

        final String newPassword = "masterchief";
        p.setPassword(newPassword);
        personService.update(p);

        final Person result = personService.findByUsername(p.getUsername());
        assertTrue(passwordEncoder.matches(newPassword, result.getPassword()));
    }

    @Test
    public void updateThrowsIllegalArgumentWhenPersonUriChanges() {
        final Person p = Generator.getPerson();
        personService.persist(p);

        p.setUri(Generator.generateUri());
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot update person's URI. Person: " + p);
        personService.update(p);
    }
}