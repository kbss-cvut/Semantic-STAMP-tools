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

import static org.junit.Assert.*;

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

    @Test
    public void findLatestRevisionReturnReportWithHighestRevisionInChain() {
        final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
        dao.persist(chain);
        final OccurrenceReport latest = chain.get(chain.size() - 1);

        final OccurrenceReport result = dao.findLatestRevision(latest.getFileNumber());
        assertEquals(latest.getUri(), result.getUri());
        assertEquals(latest.getRevision(), result.getRevision());
    }

    @Test
    public void findLatestRevisionReturnsNullForUnknownReportChain() {
        final OccurrenceReport result = dao.findLatestRevision(Long.MAX_VALUE);
        assertNull(result);
    }

    @Test
    public void findRevisionReturnsSpecificReportRevision() {
        final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
        dao.persist(chain);
        final OccurrenceReport report = chain.get(Generator.randomInt(chain.size()));

        final OccurrenceReport result = dao.findRevision(report.getFileNumber(), report.getRevision());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
        assertEquals(report.getFileNumber(), result.getFileNumber());
        assertEquals(report.getRevision(), result.getRevision());
    }

    @Test
    public void findRevisionReturnsNullForUnknownRevision() {
        final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
        dao.persist(chain);
        final Integer revision = chain.get(chain.size() - 1).getRevision() + 1;

        assertNull(dao.findRevision(chain.get(0).getFileNumber(), revision));
    }

    @Test
    public void removeReportChainDeletesAllReportsInChain() {
        final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
        dao.persist(chain);
        final Long fileNumber = chain.get(0).getFileNumber();

        dao.removeReportChain(fileNumber);
        chain.forEach(r -> {
            assertNull(dao.find(r.getUri()));
            assertFalse(dao.exists(r.getUri()));
        });
    }

    @Test
    public void removeChainDoesNothingWhenChainDoesNotExist() {
        final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
        dao.persist(chain);

        dao.removeReportChain(Long.MAX_VALUE);
        chain.forEach(r -> assertNotNull(dao.find(r.getUri())));
    }
}