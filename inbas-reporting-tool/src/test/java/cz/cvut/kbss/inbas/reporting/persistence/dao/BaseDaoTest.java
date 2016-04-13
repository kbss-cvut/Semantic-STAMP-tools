package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.persistence.BaseDaoTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
                if (p.valueEquals(pp)) {
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
            p.setUsername("user" + i + "@kbss.felk.cvu.cz");
            instances.add(p);
        }
        return instances;
    }
}
