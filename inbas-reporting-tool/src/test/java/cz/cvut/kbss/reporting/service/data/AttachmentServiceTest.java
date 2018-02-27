package cz.cvut.kbss.reporting.service.data;

import cz.cvut.kbss.reporting.environment.config.PropertyMockingApplicationContextInitializer;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.exception.AttachmentException;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.Resource;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.reporting.service.ConfigReader;
import cz.cvut.kbss.reporting.service.repository.RepositoryOccurrenceReportService;
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

import static cz.cvut.kbss.reporting.util.ConfigParam.ATTACHMENT_DIR;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(initializers = PropertyMockingApplicationContextInitializer.class)
public class AttachmentServiceTest extends BaseServiceTestRunner {

    private static final String DATA = "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";

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

    @Before
    public void setUp() throws Exception {
        Person author = persistPerson();
        cz.cvut.kbss.reporting.environment.util.Environment.setCurrentUser(author);
        this.testFile = File.createTempFile("attachmentTest", ".jpg");
        testFile.deleteOnExit();
        Files.write(testFile.toPath(), DATA.getBytes(), StandardOpenOption.APPEND);
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
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
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
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        reportService.persist(report);
        service.addAttachment(report, testFile.getName(), new FileInputStream(testFile));
        final String path = Long.toString(report.getFileNumber()) + File.separator + report.getKey();
        final File targetDir = new File(configReader.getConfig(ATTACHMENT_DIR) + File.separator + path);
        assertTrue(targetDir.exists());
        assertTrue(targetDir.isDirectory());
        final File attachment = new File(targetDir.getAbsolutePath() + File.separator + testFile.getName());
        assertTrue(attachment.exists());
        final byte[] content = Files.readAllBytes(attachment.toPath());
        assertTrue(Arrays.equals(Files.readAllBytes(testFile.toPath()), content));
    }

    @Test
    public void addAttachmentThrowsExceptionWhenAttachmentFileWithSameNameAlreadyExists() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        reportService.persist(report);
        copyAttachment(report);
        thrown.expect(AttachmentException.class);
        thrown.expectMessage(containsString("Attachment file " + testFile.getName() + " already exists"));
        service.addAttachment(report, testFile.getName(), new FileInputStream(testFile));
    }

    private void copyAttachment(OccurrenceReport report) throws Exception {
        final String attPath = Long.toString(report.getFileNumber()) + File.separator + report.getKey();
        final File targetDir = new File(configReader.getConfig(ATTACHMENT_DIR) + File.separator + attPath);
        Files.createDirectories(targetDir.toPath());
        final File targetFile = new File(targetDir.getAbsolutePath() + File.separator + testFile.getName());
        Files.copy(testFile.toPath(), targetFile.toPath());
    }

    @Test
    public void addAttachmentAddsResourceToReport() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        final int refCount = report.getReferences() != null ? report.getReferences().size() : 0;
        reportService.persist(report);
        service.addAttachment(report, testFile.getName(), new FileInputStream(testFile));

        final OccurrenceReport result = reportService.find(report.getUri());
        assertEquals(refCount + 1, result.getReferences().size());
        assertTrue(result.getReferences().stream().anyMatch(r -> r.getReference().equals(testFile.getName())));
    }

    @Test
    public void addAttachmentAddsTypeToCreatedResource() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        reportService.persist(report);
        service.addAttachment(report, testFile.getName(), new FileInputStream(testFile));

        final OccurrenceReport result = reportService.find(report.getUri());
        final Optional<Resource> res = result.getReferences().stream().filter(r -> r.getReference().equals(testFile.getName())).findAny();
        assertTrue(res.isPresent());
        assertTrue(res.get().getTypes().contains(Vocabulary.s_c_SensoryData));
    }
}