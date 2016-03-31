package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.reporting.service.PersonService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertTrue;

public class RepositoryPersonServiceTest extends BaseServiceTestRunner {

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
    public void updateEncodesNewPassword() {
        final Person p = Generator.getPerson();
        personService.persist(p);

        final String newPassword = "masterchief";
        p.setPassword(newPassword);
        personService.update(p);

        final Person result = personService.findByUsername(p.getUsername());
        assertTrue(passwordEncoder.matches(newPassword, result.getPassword()));
    }
}