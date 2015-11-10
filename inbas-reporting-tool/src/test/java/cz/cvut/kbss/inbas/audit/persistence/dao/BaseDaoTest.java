package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.persistence.BaseDaoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author ledvima1
 */
public class BaseDaoTest extends BaseDaoTestRunner {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private PreliminaryReportDao dao;  // We're using one of the DAO implementations for the basic tests

    private Person author;

    @Before
    public void setUp() throws Exception {
        author = Generator.generatePerson();
        personDao.persist(author);
    }

    @Test
    public void existsForExistingInstanceReturnsTrue() throws Exception {
        final PreliminaryReport report = new PreliminaryReport();
        report.setOccurrence(Generator.generateOccurrence());
        report.setAuthor(author);
        dao.persist(report);
        assertNotNull(report.getUri());
        assertTrue(dao.exists(report.getUri()));
    }

    @Test
    public void findAllReturnsAllExistingInstances() {
        final List<PreliminaryReport> reports = new ArrayList<>();
        final PreliminaryReport r1 = new PreliminaryReport();
        r1.setAuthor(author);
        r1.setOccurrence(Generator.generateOccurrence());
        reports.add(r1);
        final PreliminaryReport r2 = new PreliminaryReport();
        r2.setAuthor(author);
        r2.setOccurrence(Generator.generateOccurrence());
        reports.add(r2);
        dao.persist(reports);

        final List<PreliminaryReport> result = dao.findAll();
        assertEquals(reports.size(), result.size());
        for (PreliminaryReport r : reports) {
            final PreliminaryReport matching = result.stream().filter(pr -> r.getUri().equals(pr.getUri())).findFirst()
                                                     .get();
            assertNotNull(matching);
        }
    }
}
