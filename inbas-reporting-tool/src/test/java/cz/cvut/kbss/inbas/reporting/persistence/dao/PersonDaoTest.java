package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.persistence.BaseDaoTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PersonDaoTest extends BaseDaoTestRunner {

    @Autowired
    private PersonDao dao;

    @Test
    public void persistGeneratesInstanceUri() {
        final Person p = Generator.getPerson();
        assertNull(p.getUri());
        dao.persist(p);
        assertNotNull(p.getUri());
    }
}