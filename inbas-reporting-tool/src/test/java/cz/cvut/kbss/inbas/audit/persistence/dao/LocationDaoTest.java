package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Location;
import cz.cvut.kbss.inbas.audit.persistence.BaseDaoTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LocationDaoTest extends BaseDaoTestRunner {

    @Autowired
    private LocationDao dao;

    @Test
    public void persistingLocationWithoutUriGeneratesOne() throws Exception {
        final Location location = new Location();
        location.setName("LKPR");
        dao.persist(location);
        assertNotNull(location.getUri());

        final Location res = dao.find(location.getUri());
        assertNotNull(res);
        assertEquals(location, res);
    }
}