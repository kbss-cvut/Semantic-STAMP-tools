package cz.cvut.kbss.reporting.service.repository;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.exception.AuthorizationException;
import cz.cvut.kbss.reporting.exception.UsernameExistsException;
import cz.cvut.kbss.reporting.exception.ValidationException;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.security.model.AuthenticationToken;
import cz.cvut.kbss.reporting.security.model.UserDetails;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.reporting.service.PersonService;
import cz.cvut.kbss.reporting.service.event.LoginAttemptsThresholdExceeded;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

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

    @Test(expected = UsernameExistsException.class)
    public void persistThrowsUsernameExistsForUserWithDuplicateUsername() {
        final Person p = Generator.getPerson();
        personService.persist(p);
        final Person duplicate = new Person();
        duplicate.setUsername(p.getUsername());
        duplicate.setFirstName("duplicate");
        duplicate.setLastName("duplicated");
        duplicate.setPassword(Generator.PASSWORD);
        personService.persist(duplicate);
    }

    @Test(expected = ValidationException.class)
    public void persistThrowsValidationExceptionForInstanceWithoutPassword() {
        final Person p = Generator.getPerson();
        p.setPassword("");
        personService.persist(p);
    }

    @Test
    public void tryingToUpdateDifferentThanCurrentUserThrowsException() {
        final Person current = persistPerson();
        Environment.setCurrentUser(current);
        final Person update = Generator.getPerson();
        update.setUri(Generator.generateUri());
        thrown.expect(AuthorizationException.class);
        thrown.expectMessage(containsString("Modifying other user\'s account is forbidden."));
        personService.update(update);
    }

    @Test
    public void updateEncodesNewPassword() {
        final Person current = persistPerson();
        Environment.setCurrentUser(current);
        final Person update = clonePerson(current);

        final String newPassword = "masterchief";
        update.setPassword(newPassword);
        personService.update(update);

        final Person result = personService.findByUsername(update.getUsername());
        assertTrue(passwordEncoder.matches(newPassword, result.getPassword()));
    }

    private Person clonePerson(Person orig) {
        final Person copy = new Person();
        copy.setUri(orig.getUri());
        copy.setFirstName(orig.getFirstName());
        copy.setLastName(orig.getLastName());
        copy.setUsername(orig.getUsername());
        copy.setPassword(orig.getPassword());
        return copy;
    }

    @Test
    public void updateUsesOriginalPasswordWhenUpdateDoesNotContainPassword() {
        final Person current = persistPerson();
        Environment.setCurrentUser(current);
        final String originalPassword = current.getPassword();
        final Person update = clonePerson(current);
        final String newName = "UpdatedFirstName";
        update.setFirstName(newName);
        update.setPassword(null);
        personService.update(update);

        final Person result = personService.find(current.getUri());
        assertEquals(originalPassword, result.getPassword());
        assertEquals(newName, result.getFirstName());
    }

    @Test
    public void updateReloadsCurrentSecurityContext() {
        final Person current = persistPerson();
        Environment.setCurrentUser(current);
        final Principal originalPrincipal = Environment.getCurrentUserPrincipal();
        final Person update = clonePerson(current);
        final String updatedFirstName = "updatedFirstName";
        final String updatedPassword = "updatedPassword";
        update.setFirstName(updatedFirstName);
        update.setPassword(updatedPassword);
        personService.update(update);

        final AuthenticationToken updated = (AuthenticationToken) Environment.getCurrentUserPrincipal();
        assertNotEquals(originalPrincipal, updated);
        assertEquals(update.getFirstName(), ((UserDetails) updated.getDetails()).getUser().getFirstName());
    }

    @Test
    public void updateFailsWhenUsernameAlreadyExists() {
        final Person current = persistPerson();
        final Person anotherPerson = new Person();
        anotherPerson.setFirstName("another");
        anotherPerson.setLastName("Person");
        anotherPerson.setUsername("anotherOne@djkhaled.com");
        anotherPerson.setPassword("anotherOne");
        personService.persist(anotherPerson);
        Environment.setCurrentUser(current);
        final Person update = clonePerson(current);
        update.setUsername(anotherPerson.getUsername());
        thrown.expect(UsernameExistsException.class);
        thrown.expectMessage(containsString(update.getUsername() + " already exists"));

        personService.update(update);
    }

    @Test
    public void existsReturnsTrueForExistingUsername() {
        final Person person = persistPerson();
        assertTrue(personService.exists(person.getUsername()));
    }

    @Test
    public void addsLockedClassToUserWhenLoginLimitExceededEventIsReceivedForHim() {
        final Person person = persistPerson();
        assertFalse(person.getTypes().contains(Vocabulary.s_c_locked));
        final LoginAttemptsThresholdExceeded event = new LoginAttemptsThresholdExceeded(this, person);
        ((RepositoryPersonService) personService).onApplicationEvent(event);

        final Person result = personService.find(person.getUri());
        assertTrue(result.getTypes().contains(Vocabulary.s_c_locked));
    }
}