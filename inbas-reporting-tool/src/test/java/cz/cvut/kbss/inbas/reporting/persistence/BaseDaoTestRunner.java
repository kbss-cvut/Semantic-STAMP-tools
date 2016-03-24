package cz.cvut.kbss.inbas.reporting.persistence;

import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.persistence.dao.PersonDao;
import cz.cvut.kbss.inbas.reporting.test.config.TestPersistenceConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class BaseDaoTestRunner {

    @Autowired
    private PersonDao personDao;

    protected void persistPerson(Person person) {
        personDao.persist(person);
    }
}
