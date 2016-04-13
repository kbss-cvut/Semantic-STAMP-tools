package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.exception.UnsupportedReportTypeException;
import cz.cvut.kbss.inbas.reporting.model.*;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceReportDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MainReportServiceTest extends BaseServiceTestRunner {

    // More tests should be added as additional support for additional report types is added

    @Autowired
    private ReportBusinessService reportService;

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;

    @Autowired
    private OccurrenceService occurrenceService;

    private Person author;

    @Before
    public void setUp() {
        this.author = persistPerson();
        Environment.setCurrentUser(author);
    }

    @Test
    public void testPersistOccurrenceReport() {
        final OccurrenceReport report = persistOccurrenceReport();

        final OccurrenceReport result = occurrenceReportDao.find(report.getUri());
        assertNotNull(result);
    }

    private OccurrenceReport persistOccurrenceReport() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(false);
        reportService.persist(report);
        return report;
    }

    @Test(expected = UnsupportedReportTypeException.class)
    public void persistThrowsUnsupportedReportTypeForUnsupportedReportType() {
        final Report report = new Report(Generator.generateOccurrenceReport(false));
        reportService.persist(report);
    }

    @Test
    public void testFindOccurrenceReportByKey() {
        final OccurrenceReport report = persistOccurrenceReport();

        final OccurrenceReport result = reportService.findByKey(report.getKey());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    @Test(expected = UnsupportedReportTypeException.class)
    public void findByKeyThrowsUnsupportedReportTypeForReportOfUnknownType() {
        // Occurrences have keys, so it will play a role of unsupported report here
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrenceService.persist(occurrence);

        reportService.findByKey(occurrence.getKey());
    }

    @Test
    public void findByKeyReturnsNullForUnknownKey() {
        assertNull(reportService.findByKey("unknownKey"));
    }

    @Test
    public void testUpdateOccurrenceReport() {
        final OccurrenceReport report = persistOccurrenceReport();
        final String summary = "Occurrence report summary.";
        report.setSummary(summary);
        reportService.update(report);

        final OccurrenceReport result = occurrenceReportDao.findByKey(report.getKey());
        assertEquals(summary, result.getSummary());
    }

    @Test(expected = UnsupportedReportTypeException.class)
    public void updateThrowsUnsupportedReportForUnsupportedReportType() {
        final OccurrenceReport report = persistOccurrenceReport();
        final Report unsupported = new Report(report);
        reportService.update(unsupported);
    }

    @Test
    public void testFindLatestRevisionForOccurrenceReportChain() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final OccurrenceReport latest = chain.get(chain.size() - 1);
        final Long fileNumber = latest.getFileNumber();

        final LogicalDocument result = reportService.findLatestRevision(fileNumber);
        assertNotNull(result);
        assertEquals(latest.getUri(), result.getUri());
        assertEquals(latest.getKey(), result.getKey());
    }

    private List<OccurrenceReport> persistOccurrenceReportChain() {
        final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
        occurrenceReportDao.persist(chain);
        return chain;
    }

    @Test
    public void findLatestRevisionReturnsNullForUnknownReportChain() {
        assertNull(reportService.findLatestRevision(Long.MAX_VALUE));
    }

    @Test
    public void removeReportChainForOccurrenceReportRemovesAllReportsInChain() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final Long fileNumber = chain.get(0).getFileNumber();
        reportService.removeReportChain(fileNumber);

        chain.forEach(r -> assertFalse(occurrenceReportDao.exists(r.getUri())));
    }

    @Test
    public void removeReportChainDoesNothingWhenReportChainDoesNotExist() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        reportService.removeReportChain(Long.MAX_VALUE);

        chain.forEach(r -> assertTrue(occurrenceReportDao.exists(r.getUri())));
    }

    @Test
    public void getChainRevisionsReturnsListOfRevisionInfosForChainOrderedByRevisionDescending() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final Long fileNumber = chain.get(0).getFileNumber();
        // Sort descending by revision number
        Collections.sort(chain, (a, b) -> b.getRevision().compareTo(a.getRevision()));

        final List<ReportRevisionInfo> revisions = reportService.getReportChainRevisions(fileNumber);
        assertEquals(chain.size(), revisions.size());
        for (int i = 0; i < chain.size(); i++) {
            assertEquals(chain.get(i).getUri(), revisions.get(i).getUri());
            assertEquals(chain.get(i).getKey(), revisions.get(i).getKey());
            assertEquals(chain.get(i).getRevision(), revisions.get(i).getRevision());
            assertEquals(chain.get(i).getDateCreated(), revisions.get(i).getCreated());
        }
    }

    @Test
    public void testCreateNewRevisionOfOccurrenceReport() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final Long fileNumber = chain.get(0).getFileNumber();
        final OccurrenceReport latest = chain.get(chain.size() - 1);

        final OccurrenceReport newRevision = reportService.createNewRevision(fileNumber);
        assertNotNull(newRevision);
        assertEquals(latest.getRevision() + 1, newRevision.getRevision().intValue());
        assertEquals(fileNumber, newRevision.getFileNumber());

        final OccurrenceReport result = occurrenceReportDao.find(newRevision.getUri());
        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void createNewRevisionThrowsNotFoundForUnknownReportChainIdentifier() {
        reportService.createNewRevision(Long.MAX_VALUE);
    }

    @Test
    public void testFindRevisionForOccurrenceReport() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final OccurrenceReport report = Environment.randomElement(chain);

        final OccurrenceReport result = reportService.findRevision(report.getFileNumber(), report.getRevision());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    @Test
    public void findRevisionReturnsNullForUnknownRevision() {
        assertNull(reportService.findRevision(Long.MAX_VALUE, Integer.MAX_VALUE));
    }
}