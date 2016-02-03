package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.environment.util.Environment;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.exception.InvestigationExistsException;
import cz.cvut.kbss.inbas.audit.exception.NotFoundException;
import cz.cvut.kbss.inbas.audit.exception.ValidationException;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.persistence.dao.InvestigationReportDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PreliminaryReportDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

public class MainReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private PreliminaryReportDao preliminaryReportDao;
    @Autowired
    private InvestigationReportDao investigationDao;
    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private ReportService reportService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Environment.setCurrentUser(person);
    }

    @Test
    public void testFindPreliminaryReport() {
        final PreliminaryReport pr = persistPreliminaryReport();

        final Report result = reportService.find(pr.getUri());
        assertNotNull(result);
        assertEquals(pr.getUri(), result.getUri());
        assertTrue(result instanceof PreliminaryReport);
    }

    private PreliminaryReport persistPreliminaryReport() {
        final PreliminaryReport pr = Generator.generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        preliminaryReportDao.persist(pr);
        return pr;
    }

    @Test
    public void testFindInvestigation() {
        final InvestigationReport investigation = persistInvestigation();

        final Report result = reportService.find(investigation.getUri());
        assertNotNull(result);
        assertEquals(investigation.getUri(), result.getUri());
        assertTrue(result instanceof InvestigationReport);
    }

    private InvestigationReport persistInvestigation() {
        final InvestigationReport investigation = Generator.generateMinimalInvestigation();
        occurrenceDao.persist(investigation.getOccurrence());
        investigationDao.persist(investigation);
        return investigation;
    }

    @Test
    public void testFindPreliminaryReportByKey() {
        final PreliminaryReport pr = persistPreliminaryReport();

        final Report result = reportService.findByKey(pr.getKey());
        assertNotNull(result);
        assertEquals(pr.getUri(), result.getUri());
        assertTrue(result instanceof PreliminaryReport);
    }

    @Test
    public void testFindInvestigationByKey() {
        final InvestigationReport investigation = persistInvestigation();

        final Report result = reportService.findByKey(investigation.getKey());
        assertNotNull(result);
        assertEquals(investigation.getUri(), result.getUri());
        assertTrue(result instanceof InvestigationReport);
    }

    @Test
    public void findAllWithNullTypeReturnsSameAsFindAllWithoutArgs() {
        final PreliminaryReport pr = persistPreliminaryReport();
        final InvestigationReport investigation = persistInvestigation();
        final Set<URI> uris = new HashSet<>(Arrays.asList(pr.getUri(), investigation.getUri()));

        final List<OccurrenceReport> allByType = reportService.findAll(null);
        final List<OccurrenceReport> all = reportService.findAll();
        assertEquals(uris.size(), allByType.size());
        assertEquals(uris.size(), all.size());
        allByType.forEach(report -> assertTrue(uris.contains(report.getUri())));
        all.forEach(report -> assertTrue(uris.contains(report.getUri())));
    }

    @Test
    public void removeReportChainRemovesAllReportsInChain() {
        final List<Report> chain = persistReportChain();
        reportService.removeReportChain(chain.get(0).getFileNumber());

        for (Report r : chain) {
            assertNull(reportService.find(r.getUri()));
        }
    }

    private List<Report> persistReportChain() {
        final List<Report> reports = new ArrayList<>();
        PreliminaryReport prevRevision = Generator
                .generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        prevRevision.setCreated(new Date());
        preliminaryReportDao.persist(prevRevision);
        final int cnt = Generator.randomInt(5);
        final List<PreliminaryReport> toPersist = new ArrayList<>(cnt);
        for (int i = 0; i < cnt; i++) {
            reports.add(prevRevision);
            final PreliminaryReport nextRevision = new PreliminaryReport(prevRevision);
            nextRevision.setRevision(prevRevision.getRevision() + 1);
            nextRevision.setAuthor(person);
            nextRevision.setCreated(new Date());
            toPersist.add(nextRevision);
            prevRevision = nextRevision;
        }
        reports.add(prevRevision);
        preliminaryReportDao.persist(toPersist);
        InvestigationReport prevInvestigation = new InvestigationReport(toPersist.get(cnt - 1));
        prevInvestigation.setRootFactor(new Factor());
        prevInvestigation.getRootFactor().setStartTime(new Date());
        prevInvestigation.getRootFactor().setEndTime(new Date());
        prevInvestigation.setCreated(new Date());
        prevInvestigation.setAuthor(person);
        investigationDao.persist(prevInvestigation);
        final List<InvestigationReport> toPersistInv = new ArrayList<>(cnt);
        for (int i = 0; i < cnt; i++) {
            reports.add(prevInvestigation);
            final InvestigationReport nextInvestigation = new InvestigationReport(prevInvestigation);
            nextInvestigation.setRevision(prevInvestigation.getRevision() + 1);
            nextInvestigation.setAuthor(person);
            nextInvestigation.setCreated(new Date());
            nextInvestigation.setRootFactor(new Factor());
            nextInvestigation.getRootFactor().setStartTime(new Date());
            nextInvestigation.getRootFactor().setEndTime(new Date());
            toPersistInv.add(nextInvestigation);
            prevInvestigation = nextInvestigation;
        }
        reports.add(prevInvestigation);
        investigationDao.persist(toPersistInv);
        return reports;
    }

    @Test(expected = NotFoundException.class)
    public void removeReportChainWithUnknownFileNumberThrowsNotFoundException() throws Exception {
        final Long unknownFileNumber = 998877L;
        reportService.removeReportChain(unknownFileNumber);
    }

    @Test
    public void createNewRevisionCreatesNewRevisionFromLatestExistingReport() {
        final PreliminaryReport rOne = Generator.generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        final PreliminaryReport rTwo = new PreliminaryReport(rOne);
        rTwo.setAuthor(Generator.getPerson());
        rTwo.setRevision(rOne.getRevision() + 1);
        rTwo.setSummary("Different summary.");  // Differ from rOne
        preliminaryReportDao.persist(Arrays.asList(rOne, rTwo));

        final Report result = reportService.createNewRevision(rOne.getFileNumber());
        assertNotNull(result);
        assertTrue(result instanceof PreliminaryReport);
        final PreliminaryReport res = (PreliminaryReport) result;
        assertEquals(rTwo.getSummary(), res.getSummary());
        assertEquals(rTwo.getRevision() + 1, res.getRevision().intValue());
    }

    @Test
    public void createNewRevisionFromInvestigationCreatesNewRevision() {
        final List<Report> reports = persistReportChain();
        Collections.sort(reports, (a, b) -> b.getRevision() - a.getRevision());
        final InvestigationReport latest = (InvestigationReport) reports.get(0);

        final Report result = reportService.createNewRevision(latest.getFileNumber());
        assertNotNull(result);
        assertTrue(result instanceof InvestigationReport);
        assertEquals(latest.getRevision() + 1, result.getRevision().intValue());
    }

    @Test(expected = NotFoundException.class)
    public void createNewRevisionFromUnknownChainThrowsNotFoundException() {
        final Long unknownFileNumber = 998877L;
        reportService.createNewRevision(unknownFileNumber);
    }

    @Test
    public void findRevisionReturnsReportWithMatchingFileNumberAndRevision() {
        final List<Report> reports = persistReportChain();

        for (Report r : reports) {
            final Report result = reportService.findRevision(r.getFileNumber(), r.getRevision());
            assertNotNull(result);
            assertEquals(r.getUri(), result.getUri());
            assertEquals(r.getKey(), result.getKey());
        }
    }

    @Test
    public void findRevisionReturnsNullWhenReportChainDoesNotExist() {
        final List<Report> reports = persistReportChain();
        assertNull(reportService.findRevision(reports.get(0).getFileNumber() + 1, reports.get(0).getRevision()));
    }

    @Test
    public void findRevisionReturnsNullWhenRevisionDoesNotExist() {
        final List<Report> reports = persistReportChain();
        Collections.sort(reports, (a, b) -> b.getRevision() - a.getRevision());
        final InvestigationReport latest = (InvestigationReport) reports.get(0);

        assertNull(reportService.findRevision(latest.getFileNumber(), latest.getRevision() + 1));
    }

    @Test(expected = NotFoundException.class)
    public void updateThrowsNotFoundWhenOriginalReportCannotBeFound() {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        final String key = "117";
        report.setKey(key);

        reportService.update(report);
    }

    @Test(expected = ValidationException.class)
    public void updateThrowsValidationExceptionWhenOriginalAndUpdateUrisDontMatch() {
        final PreliminaryReport original = persistPreliminaryReport();
        final PreliminaryReport update = new PreliminaryReport(original);
        update.setKey(original.getKey());
        update.setUri(URI.create("http://different.uri.org"));

        reportService.update(update);
    }

    @Test
    public void updateUpdatesPreliminaryReport() {
        final PreliminaryReport report = persistPreliminaryReport();
        final String summaryUpdate = "Different summary after update.";
        report.setSummary(summaryUpdate);

        reportService.update(report);

        final PreliminaryReport result = preliminaryReportDao.find(report.getUri());
        assertEquals(summaryUpdate, result.getSummary());
    }

    @Test
    public void updateUpdatesInvestigation() {
        final InvestigationReport report = persistInvestigation();
        final String summaryUpdate = "Different summary after update.";
        report.setSummary(summaryUpdate);

        reportService.update(report);

        final InvestigationReport result = investigationDao.find(report.getUri());
        assertEquals(summaryUpdate, result.getSummary());
    }

    @Test(expected = InvestigationExistsException.class)
    public void startInvestigationThrowsInvestigationExistsExceptionWhenInvestigationAlreadyExists() {
        final List<Report> reportChain = persistReportChain();
        assertEquals(ReportingPhase.INVESTIGATION, reportChain.get(reportChain.size() - 1).getPhase());
        reportService.startInvestigation(reportChain.get(0).getFileNumber());
    }

    @Test(expected = NotFoundException.class)
    public void startInvestigationThrowsNotFoundWhenReportChainCannotBeFound() {
        final Long unknownFileNumber = 998877L;
        reportService.startInvestigation(unknownFileNumber);
    }

    @Test
    public void startInvestigationCreatesNewInvestigationForReportChain() {
        final PreliminaryReport report = persistPreliminaryReport();
        assertEquals(ReportingPhase.PRELIMINARY, report.getOccurrence().getReportingPhase());

        final InvestigationReport investigation = reportService.startInvestigation(report.getFileNumber());
        assertNotNull(investigation);
        assertEquals(report.getRevision() + 1, investigation.getRevision().intValue());
        assertEquals(ReportingPhase.INVESTIGATION, investigation.getOccurrence().getReportingPhase());
    }

    @Test
    public void reportChainUsesTheSameFileNumber() {
        final List<Report> result = persistReportChain();
        assertFalse(result.isEmpty());
        Long fileNumber = result.get(0).getFileNumber();
        for (Report r : result) {
            assertEquals(fileNumber, r.getFileNumber());
        }
    }
}
