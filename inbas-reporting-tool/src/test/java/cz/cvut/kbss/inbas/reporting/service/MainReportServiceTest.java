package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.UnsupportedReport;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.exception.ReportImportingException;
import cz.cvut.kbss.inbas.reporting.exception.UnsupportedReportTypeException;
import cz.cvut.kbss.inbas.reporting.model.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.AuditReportDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.SafetyIssueReportDao;
import cz.cvut.kbss.inbas.reporting.service.data.mail.ReportImporter;
import cz.cvut.kbss.inbas.reporting.service.options.ReportingPhaseService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MainReportServiceTest extends BaseServiceTestRunner {

    private static final String IMPORT_FILE_NAME = "16FEDEF0BC91E511B897002655546824-anon.e5x";

    // More tests should be added as additional support for additional report types is added

    @Autowired
    private ReportBusinessService reportService;

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;

    @Autowired
    private OccurrenceService occurrenceService;

    @Autowired
    private OccurrenceReportService occurrenceReportService;

    @Autowired
    private SafetyIssueReportDao safetyIssueReportDao;

    @Autowired
    private SafetyIssueReportService safetyIssueReportService;

    @Autowired
    private AuditReportDao auditReportDao;

    @Autowired
    private AuditReportService auditReportService;

    @Autowired
    private ReportImporter reportImporterMock;

    @Autowired
    private ReportingPhaseService phaseService;

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
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        reportService.persist(report);
        return report;
    }

    @Test(expected = UnsupportedReportTypeException.class)
    public void persistThrowsUnsupportedReportTypeForUnsupportedReportType() {
        final UnsupportedReport report = new UnsupportedReport();
        reportService.persist(report);
    }

    @Test
    public void testFindOccurrenceReportByKey() {
        final OccurrenceReport report = persistOccurrenceReport();

        final OccurrenceReport result = reportService.findByKey(report.getKey());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    @Test
    public void testFindSafetyIssueReportByKey() {
        final SafetyIssueReport report = persistSafetyIssueReport();

        final SafetyIssueReport result = reportService.findByKey(report.getKey());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    private SafetyIssueReport persistSafetyIssueReport() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(false, false);
        report.setAuthor(author);
        safetyIssueReportService.persist(report);
        return report;
    }

    @Test
    public void testFindAuditReportByKey() {
        final AuditReport report = AuditReportGenerator.generateAuditReport(false);
        report.setAuthor(author);
        auditReportService.persist(report);

        final AuditReport result = reportService.findByKey(report.getKey());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    @Test
    public void testPersistAuditReport() {
        final AuditReport report = AuditReportGenerator.generateAuditReport(false);
        report.setAuthor(author);
        reportService.persist(report);

        final AuditReport result = auditReportService.find(report.getUri());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    @Test(expected = UnsupportedReportTypeException.class)
    public void findByKeyThrowsUnsupportedReportTypeForReportOfUnknownType() {
        // Occurrences have keys, so it will play a role of unsupported report here
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
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
        final UnsupportedReport report = new UnsupportedReport();
        reportService.update(report);
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
        final List<OccurrenceReport> chain = OccurrenceReportGenerator.generateOccurrenceReportChain(author);
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

    @Test
    public void findAllReturnsLatestRevisionsOfAllReportChains() {
        // Once other report types are added, they should be added into this tests
        final List<LogicalDocument> latestRevisions = initOccurrenceReportChains();

        final List<ReportDto> result = reportService.findAll();
        assertTrue(Environment.areEqual(latestRevisions, result));
    }

    /**
     * Generates report chains.
     *
     * @return List of latest revisions of the generated chains, ordered by date created descending
     */
    private List<LogicalDocument> initOccurrenceReportChains() {
        final List<LogicalDocument> latestRevisions = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final List<OccurrenceReport> chain = persistOccurrenceReportChain();
            latestRevisions.add(chain.get(chain.size() - 1));
        }
        Collections.sort(latestRevisions, (a, b) -> b.getDateCreated().compareTo(a.getDateCreated()));
        return latestRevisions;
    }

    @Test
    public void transitionToNextPhaseTransitionsReportPhase() {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.setAuthor(author);
        report.setPhase(phaseService.getInitialPhase());
        occurrenceReportService.persist(report);

        final URI expected = phaseService.nextPhase(report.getPhase());
        reportService.transitionToNextPhase(report);

        final OccurrenceReport result = occurrenceReportDao.find(report.getUri());
        assertEquals(expected, result.getPhase());
    }

    @Test
    public void importReportFromFileImportsE5XReport() throws Exception {
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/eccairs/" + IMPORT_FILE_NAME);
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.setAuthor(author);
        when(reportImporterMock.process(any(NamedStream.class))).thenAnswer((invocation) -> {
            occurrenceReportDao.persist(report);
            return Collections.singletonList(report.getUri());
        });
        final LogicalDocument res = reportService.importReportFromFile(IMPORT_FILE_NAME, is);
        assertNotNull(res);
        assertTrue(res instanceof OccurrenceReport);
        final OccurrenceReport resultReport = reportService.findByKey(res.getKey());
        assertEquals(report.getUri(), resultReport.getUri());
    }

    @Test(expected = ReportImportingException.class)
    public void importReportThrowsReportImportingExceptionWhenImportFails() throws Exception {
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/eccairs/" + IMPORT_FILE_NAME);
        when(reportImporterMock.process(any(NamedStream.class))).thenThrow(new IOException("Unable to load report."));
        reportService.importReportFromFile(IMPORT_FILE_NAME, is);
    }

    @Test
    public void importReportReturnsTheFirstReportWhenMultipleAreExtractedFromArchive() throws Exception {
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/eccairs/" + IMPORT_FILE_NAME);
        final List<OccurrenceReport> reports = Arrays.asList(OccurrenceReportGenerator.generateOccurrenceReport(true),
                OccurrenceReportGenerator.generateOccurrenceReport(true));
        reports.forEach(r -> r.setAuthor(author));
        when(reportImporterMock.process(any(NamedStream.class))).thenAnswer((invocation) -> {
            occurrenceReportDao.persist(reports);
            return reports.stream().map(OccurrenceReport::getUri).collect(Collectors.toList());
        });
        final LogicalDocument res = reportService.importReportFromFile(IMPORT_FILE_NAME, is);
        assertNotNull(res);
        assertTrue(res instanceof OccurrenceReport);
        final OccurrenceReport resultReport = reportService.findByKey(res.getKey());
        assertEquals(reports.get(0).getUri(), resultReport.getUri());
    }

    @Test
    public void findAllLoadsLatestRevisionsOfAllKindsOfReports() {
        final List<LogicalDocument> latestRevisions = initReportChainsForFindAll();
        final List<ReportDto> result = reportService.findAll();
        assertTrue(Environment.areEqual(latestRevisions, result));
    }

    private List<LogicalDocument> initReportChainsForFindAll() {
        final List<LogicalDocument> list = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(1, 10); i++) {
            final List<OccurrenceReport> chain = OccurrenceReportGenerator.generateOccurrenceReportChain(author);
            occurrenceReportDao.persist(chain);
            list.add(chain.get(chain.size() - 1));
        }
        for (int i = 0; i < Generator.randomInt(1, 10); i++) {
            final List<SafetyIssueReport> chain = SafetyIssueReportGenerator.generateSafetyIssueReportChain(author);
            safetyIssueReportDao.persist(chain);
            list.add(chain.get(chain.size() - 1));
        }
        for (int i = 0; i < Generator.randomInt(1, 10); i++) {
            final List<AuditReport> chain = AuditReportGenerator.generateAuditReportChain(author);
            auditReportDao.persist(chain);
            list.add(chain.get(chain.size() - 1));
        }
        return list;
    }
}
