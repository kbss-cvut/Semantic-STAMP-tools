package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.persistence.BaseDaoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseReportDaoTest extends BaseDaoTestRunner {

    @Autowired
    private OccurrenceReportDao dao;    // Use this implementation for the tests (make sure tested methods are not overridden)

    private Person author;

    @Before
    public void setUp() {
        this.author = Generator.getPerson();
        persistPerson(author);
    }

    @Test
    public void findAllGetsReportsOrderedByOccurrenceStartDescending() {
        final List<OccurrenceReport> reports = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final OccurrenceReport r = Generator.generateOccurrenceReport(true);
            r.setAuthor(author);
            r.setOccurrenceStart(new Date(System.currentTimeMillis() + i * 1000));
            reports.add(r);
        }
        dao.persist(reports);
        Collections.reverse(reports);   // First report will have highest start date

        final List<OccurrenceReport> result = dao.findAll();
        assertEquals(reports.size(), result.size());
        for (int i = 0; i < reports.size(); i++) {
            assertEquals(reports.get(i).getUri(), result.get(i).getUri());
        }
    }

    @Test
    public void findAllGetsLatestRevisionsForEveryReportChain() {
        final Set<URI> latestRevisionUris = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
            dao.persist(chain);
            latestRevisionUris.add(chain.get(chain.size() - 1).getUri());   // Get latest revision URI
        }

        final List<OccurrenceReport> result = dao.findAll();
        assertEquals(latestRevisionUris.size(), result.size());
        result.forEach(r -> assertTrue(latestRevisionUris.contains(r.getUri())));
    }
}