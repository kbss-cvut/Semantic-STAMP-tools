package cz.cvut.kbss.reporting.service.data;

import cz.cvut.kbss.reporting.environment.config.PropertyMockingApplicationContextInitializer;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.exception.AttachmentException;
import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.model.*;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.reporting.service.ConfigReader;
import cz.cvut.kbss.reporting.service.event.ReportChainRemovalEvent;
import cz.cvut.kbss.reporting.service.event.ResourceRemovalEvent;
import cz.cvut.kbss.reporting.service.repository.RepositoryOccurrenceReportService;
import cz.cvut.kbss.reporting.util.IdentificationUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

import static cz.cvut.kbss.reporting.environment.util.Environment.DATA;
import static cz.cvut.kbss.reporting.util.ConfigParam.ATTACHMENT_DIR;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

@ContextConfiguration(initializers = PropertyMockingApplicationContextInitializer.class)
public class AttachmentServiceTest extends BaseServiceTestRunner {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private AttachmentService service;

    @Autowired
    private RepositoryOccurrenceReportService reportService;

    @Autowired
    private ConfigReader configReader;

    @Autowired
    private Environment environment;

    private File testFile;

    private OccurrenceReport report;

    @Before
    public void setUp() throws Exception {
        Person author = persistPerson();
        cz.cvut.kbss.reporting.environment.util.Environment.setCurrentUser(author);
        this.testFile = File.createTempFile("attachmentTest", ".jpg");
        testFile.deleteOnExit();
        Files.write(testFile.toPath(), DATA.getBytes(), StandardOpenOption.APPEND);
        this.report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.setKey(IdentificationUtils.generateKey());
        final File attachmentsDir = Files.createTempDirectory("rt-attachments").toFile();
        attachmentsDir.deleteOnExit();
        ((MockEnvironment) environment)
                .setProperty(configReader.getConfig(ATTACHMENT_DIR), attachmentsDir.getAbsolutePath());
    }

    @After
    public void tearDown() throws Exception {
        if (!configReader.getConfig(ATTACHMENT_DIR).isEmpty()) {
            final File dir = new File(configReader.getConfig(ATTACHMENT_DIR));
            Files.walk(dir.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }
    }

    @Test
    public void addAttachmentCreatesAttachmentsDirectoryWhenItDoesNotExist() throws Exception {
        reportService.persist(report);
        service.addAttachment(report, testFile.getName(), new FileInputStream(testFile));
        final File attachmentsDir = new File(configReader.getConfig(ATTACHMENT_DIR));
        assertTrue(attachmentsDir.exists());
        assertTrue(attachmentsDir.isDirectory());
    }

    @Test
    public void addAttachmentThrowsExceptionWhenAttachmentsDirectoryIsNotConfigured() throws Exception {
        ((MockEnvironment) environment).setProperty(ATTACHMENT_DIR.toString(), "");
        thrown.expect(AttachmentException.class);
        thrown.expectMessage(containsString("Attachments directory not configured"));
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        reportService.persist(report);
        service.addAttachment(report, testFile.getName(), new FileInputStream(testFile));
    }

    @Test
    public void addAttachmentSavesFileToDirectoryCorrespondingToReportFileNumberAndKey() throws Exception {
        reportService.persist(report);
        service.addAttachment(report, testFile.getName(), new FileInputStream(testFile));
        final File targetDir = new File(reportAttachmentsPath(report));
        assertTrue(targetDir.exists());
        assertTrue(targetDir.isDirectory());
        final File attachment = new File(targetDir.getAbsolutePath() + File.separator + testFile.getName());
        assertTrue(attachment.exists());
        final byte[] content = Files.readAllBytes(attachment.toPath());
        assertTrue(Arrays.equals(Files.readAllBytes(testFile.toPath()), content));
    }

    private String reportAttachmentsPath(AbstractReport report) {
        return configReader.getConfig(ATTACHMENT_DIR) + File.separator + report.getFileNumber() + File.separator +
                report.getKey();
    }

    @Test
    public void addAttachmentThrowsExceptionWhenAttachmentFileWithSameNameAlreadyExists() throws Exception {
        reportService.persist(report);
        copyAttachment(report);
        thrown.expect(AttachmentException.class);
        thrown.expectMessage(containsString("Attachment file " + testFile.getName() + " already exists"));
        service.addAttachment(report, testFile.getName(), new FileInputStream(testFile));
    }

    private void copyAttachment(AbstractReport report) throws Exception {
        final File targetDir = new File(reportAttachmentsPath(report));
        Files.createDirectories(targetDir.toPath());
        final File targetFile = new File(targetDir.getAbsolutePath() + File.separator + testFile.getName());
        Files.copy(testFile.toPath(), targetFile.toPath());
    }

    @Test
    public void addAttachmentAddsResourceToReport() throws Exception {
        final int refCount = report.getReferences() != null ? report.getReferences().size() : 0;
        reportService.persist(report);
        service.addAttachment(report, testFile.getName(), new FileInputStream(testFile));

        final OccurrenceReport result = reportService.find(report.getUri());
        assertEquals(refCount + 1, result.getReferences().size());
        assertTrue(result.getReferences().stream().anyMatch(r -> r.getReference().equals(testFile.getName())));
    }

    @Test
    public void addAttachmentAddsTypeToCreatedResource() throws Exception {
        reportService.persist(report);
        service.addAttachment(report, testFile.getName(), new FileInputStream(testFile));

        final OccurrenceReport result = reportService.find(report.getUri());
        final Optional<Resource> res = result.getReferences().stream()
                                             .filter(r -> r.getReference().equals(testFile.getName())).findAny();
        assertTrue(res.isPresent());
        assertTrue(res.get().getTypes().contains(Vocabulary.s_c_SensoryData));
    }

    @Test
    public void getAttachmentReturnsAttachedFile() throws Exception {
        reportService.persist(report);
        copyAttachment(report);

        final File result = service.getAttachment(report, testFile.getName());
        assertNotNull(result);
        final File expected = new File(reportAttachmentsPath(report) + File.separator + testFile.getName());
        assertEquals(expected, result);
    }

    @Test
    public void getAttachmentThrowsNotFoundExceptionWhenCorrespondingFileIsNotFound() throws Exception {
        reportService.persist(report);
        copyAttachment(report);
        final String wrongName = "wrongName.jpg";
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(containsString("Attachment file " + wrongName + " not found for report " + report));

        service.getAttachment(report, wrongName);
    }

    @Test
    public void deleteAttachmentsRemovesWholeFolderRelatedToSpecifiedReport() throws Exception {
        reportService.persist(report);
        copyAttachment(report);

        service.deleteAttachments(report);
        final File reportDir = new File(reportAttachmentsPath(report));
        assertFalse(reportDir.exists());
    }

    @Test
    public void deleteAttachmentsDoesNothingWhenReportHasNone() {
        final OccurrenceReport otherReport = OccurrenceReportGenerator.generateOccurrenceReport(true);
        otherReport.setKey(IdentificationUtils.generateKey());
        service.deleteAttachments(otherReport);
        assertTrue(new File(configReader.getConfig(ATTACHMENT_DIR)).exists());
    }

    @Test
    public void deleteAttachmentsRemovesWholeChainDirectory() throws Exception {
        reportService.persist(report);
        copyAttachment(report);
        final OccurrenceReport anotherInChain = OccurrenceReportGenerator.generateOccurrenceReport(true);
        anotherInChain.setFileNumber(report.getFileNumber());
        anotherInChain.setKey(IdentificationUtils.generateKey());
        copyAttachment(anotherInChain);

        service.deleteAttachments(report.getFileNumber());
        assertFalse(new File(reportAttachmentsPath(report)).exists());
        assertFalse(new File(reportAttachmentsPath(anotherInChain)).exists());
        assertFalse(
                new File(configReader.getConfig(ATTACHMENT_DIR) + File.separator + report.getFileNumber()).exists());
    }

    @Test
    public void deleteAttachmentsDoesNothingWhenChainHasNoAttachments() {
        final Long fileNo = System.currentTimeMillis();
        service.deleteAttachments(fileNo);
        assertTrue(new File(configReader.getConfig(ATTACHMENT_DIR)).exists());
    }

    @Test
    public void deleteAttachmentRemovesAttachmentFile() throws Exception {
        reportService.persist(report);
        copyAttachment(report);
        final Resource resource = new Resource();
        resource.setReference(testFile.getName());
        report.addReference(resource);

        service.deleteAttachment(report, resource);
        assertFalse(new File(reportAttachmentsPath(report) + File.separator + testFile.getName()).exists());
    }

    @Test
    public void deleteAttachmentDoesNothingForUnknownResource() throws Exception {
        reportService.persist(report);
        copyAttachment(report);
        final Resource resource = new Resource();
        resource.setReference("unknown.png");
        report.addReference(resource);

        service.deleteAttachment(report, resource);
        // Original attachment was retained
        assertTrue(new File(reportAttachmentsPath(report) + File.separator + testFile.getName()).exists());
    }

    @Test
    public void deleteAttachmentDoesNothingForUnknownReport() {
        final OccurrenceReport otherReport = OccurrenceReportGenerator.generateOccurrenceReport(true);
        otherReport.setKey(IdentificationUtils.generateKey());
        final Resource resource = new Resource();
        resource.setReference("unknown.png");
        service.deleteAttachment(otherReport, resource);
    }

    @Test
    public void deletesReportChainAttachmentsOnChainRemovalEvent() throws Exception {
        reportService.persist(report);
        copyAttachment(report);

        service.onApplicationEvent(new ReportChainRemovalEvent(this, report.getFileNumber()));
        assertFalse(
                new File(configReader.getConfig(ATTACHMENT_DIR) + File.separator + report.getFileNumber()).exists());
    }

    @Test
    public void deletesAttachmentOnResourceRemovalEvent() throws Exception {
        reportService.persist(report);
        copyAttachment(report);
        final Resource resource = new Resource();
        resource.setReference(testFile.getName());

        service.onApplicationEvent(new ResourceRemovalEvent(this, report, resource));
        assertFalse(new File(reportAttachmentsPath(report) + File.separator + testFile.getName()).exists());
    }
}