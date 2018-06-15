package cz.cvut.kbss.reporting.service;

import cz.cvut.kbss.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.environment.config.PropertyMockingApplicationContextInitializer;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.environment.util.UnsupportedReport;
import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.exception.UnsupportedReportTypeException;
import cz.cvut.kbss.reporting.filter.OccurrenceCategoryFilter;
import cz.cvut.kbss.reporting.filter.ReportFilter;
import cz.cvut.kbss.reporting.model.*;
import cz.cvut.kbss.reporting.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.reporting.service.data.AttachmentService;
import cz.cvut.kbss.reporting.service.options.ReportingPhaseService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static cz.cvut.kbss.reporting.environment.util.Environment.DATA;
import static cz.cvut.kbss.reporting.util.ConfigParam.ATTACHMENT_DIR;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@ContextConfiguration(initializers = PropertyMockingApplicationContextInitializer.class)
public class MainReportServiceTest extends BaseServiceTestRunner {

    // More tests should be added as additional support for additional report types is added

    @Autowired
    private MainReportService reportService;

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;

    @Autowired
    private OccurrenceService occurrenceService;

    @Autowired
    private OccurrenceReportService occurrenceReportService;

    @Autowired
    private ReportingPhaseService phaseService;

    @Autowired
    private org.springframework.core.env.Environment environment;

    @Autowired
    private AttachmentService attachmentServiceSpy;

    private File testFile;

    private Person author;

    @Before
    public void setUp() throws Exception {
        this.author = persistPerson();
        Environment.setCurrentUser(author);
        this.testFile = File.createTempFile("attachmentTest", ".jpg");
        testFile.deleteOnExit();
        Files.write(testFile.toPath(), DATA.getBytes(), StandardOpenOption.APPEND);
        final File attachmentsDir = Files.createTempDirectory("rt-attachments").toFile();
        attachmentsDir.deleteOnExit();
        ((MockEnvironment) environment).setProperty(ATTACHMENT_DIR.toString(), attachmentsDir.getAbsolutePath());
    }

    @After
    public void tearDown() throws Exception {
        final File attachmentsDir = new File(environment.getProperty(ATTACHMENT_DIR.toString()));
        if (attachmentsDir.exists()) {
            Files.walk(attachmentsDir.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile)
                 .forEach(File::delete);
        }
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
        Assert.assertEquals(latest.getUri(), result.getUri());
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
        chain.sort((a, b) -> b.getRevision().compareTo(a.getRevision()));

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
        final List<LogicalDocument> latestRevisions = initReportChains();

        final List<ReportDto> result = reportService.findAll();
        assertTrue(Environment.areEqual(latestRevisions, result));
    }

    /**
     * Generates report chains.
     *
     * @return List of latest revisions of the generated chains, ordered by date created descending
     */
    private List<LogicalDocument> initReportChains() {
        final List<LogicalDocument> latestRevisions = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final List<OccurrenceReport> chain = persistOccurrenceReportChain();
            latestRevisions.add(chain.get(chain.size() - 1));
        }
        latestRevisions.sort((a, b) -> b.getDateCreated().compareTo(a.getDateCreated()));
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
    public void pageableFindAllMergesPagesFromAllRegisteredServicesAndReturnsNewPageOfSpecifiedSize() {
        final int count = 10;
        generateReports(count);
        final Pageable pageSpec = PageRequest.of(0, 5);

        final Page<ReportDto> result = reportService.findAll(pageSpec, Collections.emptyList());
        assertEquals(pageSpec.getPageSize(), result.getNumberOfElements());
    }

    private List<OccurrenceReport> generateReports(int count) {
        final List<OccurrenceReport> reports = OccurrenceReportGenerator.generateReports(true, count);
        reports.forEach(r -> r.setAuthor(author));
        occurrenceReportDao.persist(reports);
        return reports;
    }

    @Test
    public void pageableFindAllHandlesSituationWhenPageSizeIsBiggerThanResultSize() {
        final int count = 10;
        final List<OccurrenceReport> reports = generateReports(count);
        final URI occurrenceCategory = reports.get(0).getOccurrence().getEventType();
        final ReportFilter filter = ReportFilter
                .create(OccurrenceCategoryFilter.KEY, Collections.singletonList(occurrenceCategory.toString()))
                .orElseThrow(AssertionError::new);
        final Pageable pageSpec = PageRequest.of(0, count);

        final Page<ReportDto> result = reportService.findAll(pageSpec, Collections.singletonList(filter));
        assertTrue(result.getNumberOfElements() < count);
    }

    @Test
    public void removeReportChainRemovesAttachments() {
        final List<OccurrenceReport> chain = persistOccurrenceReportChain();
        final Long fileNumber = chain.get(0).getFileNumber();
        reportService.removeReportChain(fileNumber);

        verify(attachmentServiceSpy).deleteAttachments(fileNumber);
    }

    @Test
    public void addAttachmentAddsResourceToReport() throws Exception {
        final OccurrenceReport report = persistOccurrenceReport();
        final int refCount = report.getReferences() != null ? report.getReferences().size() : 0;
        reportService.addAttachment(report, testFile.getName(), null, new FileInputStream(testFile));

        final OccurrenceReport result = occurrenceReportService.find(report.getUri());
        assertEquals(refCount + 1, result.getReferences().size());
        assertTrue(result.getReferences().stream().anyMatch(r -> r.getReference().equals(testFile.getName())));
    }

    @Test
    public void addAttachmentAddsTypeToCreatedResource() throws Exception {
        final OccurrenceReport report = persistOccurrenceReport();
        reportService.addAttachment(report, testFile.getName(), null, new FileInputStream(testFile));

        final OccurrenceReport result = occurrenceReportService.find(report.getUri());
        final Optional<Resource> res = result.getReferences().stream()
                                             .filter(r -> r.getReference().equals(testFile.getName())).findAny();
        assertTrue(res.isPresent());
        assertTrue(res.get().getTypes().contains(Vocabulary.s_c_SensoryData));
    }

    @Test
    public void addAttachmentAddsDescriptionToResource() throws Exception {
        final OccurrenceReport report = persistOccurrenceReport();
        final String description = "This is an attachment description.";
        reportService.addAttachment(report, testFile.getName(), description, new FileInputStream(testFile));

        final OccurrenceReport result = occurrenceReportService.find(report.getUri());
        final Optional<Resource> res = result.getReferences().stream()
                                             .filter(r -> r.getReference().equals(testFile.getName())).findAny();
        assertTrue(res.isPresent());
        assertEquals(description, res.get().getDescription());
    }

    @Test
    public void createNewRevisionCopiesAttachments() throws Exception {
        final OccurrenceReport report = persistOccurrenceReport();
        reportService.addAttachment(report, testFile.getName(), null, new FileInputStream(testFile));

        final OccurrenceReport newRevision = reportService.createNewRevision(report.getFileNumber());
        final ArgumentCaptor<OccurrenceReport> captor = ArgumentCaptor.forClass(OccurrenceReport.class);
        verify(attachmentServiceSpy).copyAttachments(captor.capture(), captor.capture());
        assertEquals(report.getUri(), captor.getAllValues().get(0).getUri());
        assertEquals(newRevision.getUri(), captor.getAllValues().get(1).getUri());
    }

    @Test
    public void pageableFindAllReturnsPageWithCorrectTotalSumOfReports() {
        final int count = 10;
        generateReports(count);

        final Page<ReportDto> result = reportService.findAll(PageRequest.of(0, 5), Collections.emptyList());
        assertEquals(count, result.getTotalElements());
    }
}
