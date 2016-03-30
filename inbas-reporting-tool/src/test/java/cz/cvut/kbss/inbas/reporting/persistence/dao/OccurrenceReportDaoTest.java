package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.Occurrence;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.persistence.BaseDaoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

public class OccurrenceReportDaoTest extends BaseDaoTestRunner {

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;

    @Autowired
    private OccurrenceDao occurrenceDao;

    private Person author;

    @Before
    public void setUp() throws Exception {
        this.author = Generator.getPerson();
        persistPerson(author);
    }

    @Test
    public void persistNewReportPersistsOccurrenceAsWell() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setAuthor(author);
        occurrenceReportDao.persist(report);

        final Occurrence occurrence = occurrenceDao.find(report.getOccurrence().getUri());
        assertNotNull(occurrence);
    }

    @Test
    public void persistReportWithExistingOccurrenceReusesOccurrenceInstance() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setAuthor(author);
        occurrenceReportDao.persist(report);

        final OccurrenceReport newReport = new OccurrenceReport(report);
        assertSame(report.getOccurrence(), newReport.getOccurrence());
        newReport.setAuthor(author);
        newReport.setRevision(report.getRevision() + 1);
        occurrenceReportDao.persist(newReport);

        final OccurrenceReport resOrig = occurrenceReportDao.find(report.getUri());
        assertNotNull(resOrig);
        final OccurrenceReport resCopy = occurrenceReportDao.find(newReport.getUri());
        assertNotNull(resCopy);
        assertEquals(resOrig.getOccurrence().getUri(), resCopy.getOccurrence().getUri());
    }

    @Test
    public void findByOccurrenceGetsReportsWithMatchingOccurrence() {
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrenceDao.persist(occurrence);
        final List<OccurrenceReport> reports = persistReportsForOccurrence(occurrence);
        // This one is just so that the method does not simply select all reports
        final OccurrenceReport other = Generator.generateOccurrenceReport(true);
        other.setAuthor(author);
        occurrenceReportDao.persist(other);

        final List<OccurrenceReport> result = occurrenceReportDao.findByOccurrence(occurrence);
        assertTrue(Environment.areEqual(reports, result));
    }

    private List<OccurrenceReport> persistReportsForOccurrence(Occurrence occurrence) {
        final List<OccurrenceReport> reports = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final OccurrenceReport r = Generator.generateOccurrenceReport(true);
            r.setOccurrence(occurrence);
            r.setAuthor(author);
            r.setOccurrenceStart(new Date(System.currentTimeMillis() + i * 1000));
            reports.add(r);
        }
        occurrenceReportDao.persist(reports);
        return reports;
    }

    @Test
    public void findByOccurrenceReturnsLatestRevisionsOfMatchingReportChains() throws Exception {
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrenceDao.persist(occurrence);
        final Set<URI> reportUris = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final OccurrenceReport firstRev = Generator.generateOccurrenceReport(true);
            firstRev.setOccurrence(occurrence);
            firstRev.setAuthor(author);
            final List<OccurrenceReport> chain = persistReportChain(firstRev, occurrenceReportDao);
            reportUris.add(chain.get(chain.size() - 1).getUri());
        }

        final List<OccurrenceReport> result = occurrenceReportDao.findByOccurrence(occurrence);
        assertEquals(reportUris.size(), result.size());
        result.forEach(r -> assertTrue(reportUris.contains(r.getUri())));
    }

    static List<OccurrenceReport> persistReportChain(OccurrenceReport firstRevision, OccurrenceReportDao dao) {
        final List<OccurrenceReport> chain = new ArrayList<>();
        chain.add(firstRevision);
        dao.persist(firstRevision);
        OccurrenceReport previous = firstRevision;
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final OccurrenceReport r = new OccurrenceReport(previous);
            r.setRevision(previous.getRevision() + 1);
            r.setAuthor(previous.getAuthor());
            r.setDateCreated(new Date());
            dao.persist(r);
            chain.add(r);
            previous = r;
        }
        return chain;
    }

    @Test
    public void findByOccurrenceReturnsReportsOrderedByOccurrenceStartDescending() {
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrenceDao.persist(occurrence);
        final List<OccurrenceReport> reports = persistReportsForOccurrence(occurrence);
        Collections.sort(reports, (a, b) -> b.getOccurrenceStart().compareTo(a.getOccurrenceStart()));  // Descending

        final List<OccurrenceReport> result = occurrenceReportDao.findByOccurrence(occurrence);
        assertTrue(Environment.areEqual(reports, result));
    }
}
