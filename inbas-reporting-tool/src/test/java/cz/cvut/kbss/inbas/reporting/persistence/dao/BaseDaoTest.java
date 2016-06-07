package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.inbas.reporting.persistence.PersistenceException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class BaseDaoTest extends BaseDaoTestRunner {

    @Autowired
    private PersonDao personDao;

    @Test
    public void existsForExistingInstanceReturnsTrue() throws Exception {
        final Person person = Generator.getPerson();
        personDao.persist(person);
        assertTrue(personDao.exists(person.getUri()));
    }

    @Test
    public void findAllReturnsAllExistingInstances() {
        final List<Person> instances = generateInstances();
        personDao.persist(instances);
        final List<Person> result = personDao.findAll();

        assertEquals(instances.size(), result.size());
        boolean found = false;
        for (Person p : instances) {
            for (Person pp : result) {
                if (p.nameEquals(pp)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    private List<Person> generateInstances() {
        final List<Person> instances = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final Person p = new Person();
            p.setFirstName("user" + i);
            p.setLastName("lastName" + i);
            p.setUsername("user" + i + "@kbss.felk.cvut.cz");
            instances.add(p);
        }
        return instances;
    }

    @Test
    public void existsReturnsFalseForNullUri() {
        assertFalse(personDao.exists(null));
    }

    @Test
    public void removeCollectionRemovesEveryInstanceInIt() {
        final List<Person> persons = generateInstances();
        personDao.persist(persons);

        personDao.remove(persons);
        persons.forEach(p -> assertNull(personDao.find(p.getUri())));
    }

    @Test
    public void removeEmptyCollectionDoesNothing() {
        final List<Person> persons = generateInstances();
        personDao.persist(persons);

        personDao.remove(Collections.emptyList());
        persons.forEach(p -> assertNotNull(personDao.find(p.getUri())));
    }

    @Test(expected = PersistenceException.class)
    public void persistThrowsPersistenceExceptionWhenExceptionIsThrownByPersistenceProvider() {
        final Person p = new Person();
        p.setFirstName("Catherine");
        p.setLastName("Halsey");
        // No username -> IC violation
        personDao.persist(p);
    }

    @Test(expected = PersistenceException.class)
    public void updateThrowsPersistenceExceptionWhenExceptionIsThrownByPersistenceProvider() {
        final Person person = Generator.getPerson();
        personDao.persist(person);
        person.setUsername(null);
        personDao.update(person);
    }
}
