package cz.cvut.kbss.reporting.service.data;

import cz.cvut.kbss.reporting.exception.AttachmentException;
import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.model.AbstractReport;
import cz.cvut.kbss.reporting.model.Resource;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.service.ConfigReader;
import cz.cvut.kbss.reporting.service.ReportBusinessService;
import cz.cvut.kbss.reporting.util.ConfigParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

/**
 * Manages binary file attachments (images, audio, video etc.) of reports.
 */
@Service
public class AttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AttachmentService.class);

    private final ConfigReader configReader;

    private final ReportBusinessService reportService;

    @Autowired
    public AttachmentService(ConfigReader configReader, ReportBusinessService reportService) {
        this.configReader = configReader;
        this.reportService = reportService;
    }

    /**
     * Stores the specified attachment related to the specified report.
     * <p>
     * This also adds a corresponding {@link Resource} instance to the report.
     *
     * @param report   Report to which the file is attached
     * @param fileName Name of the attachment file
     * @param content  The attached file content
     * @throws AttachmentException When attachment storing fails
     */
    public void addAttachment(AbstractReport report, String fileName, InputStream content) {
        LOG.debug("Adding attachment {} to report {}.", fileName, report);
        attachFile(report, fileName, content);
        addResourceReference(report, fileName);
    }

    private void attachFile(AbstractReport report, String fileName, InputStream content) {
        final File attachmentsDir = getAttachmentsDir();
        final File targetDir = new File(generateReportAttachmentsPath(attachmentsDir, report));
        ensureDirectoryExists(targetDir);
        final File attachment = new File(targetDir + File.separator + fileName);
        ensureUnique(attachment);
        try {
            Files.copy(content, attachment.toPath());
        } catch (IOException e) {
            throw new AttachmentException("Unable to save attachment to file " + attachment.getAbsolutePath(), e);
        } finally {
            try {
                content.close();
            } catch (IOException e) {
                throw new AttachmentException("Unable to close attachment input stream.", e);
            }
        }
    }

    private File getAttachmentsDir() {
        final String targetDirPath = configReader.getConfig(ConfigParam.ATTACHMENT_DIR, "");
        if (targetDirPath.isEmpty()) {
            throw new AttachmentException("Attachments directory not configured!");
        }
        final File targetDir = new File(targetDirPath);
        ensureDirectoryExists(targetDir);
        return targetDir;
    }

    private void ensureDirectoryExists(File targetDir) {
        if (!targetDir.exists()) {
            try {
                Files.createDirectories(targetDir.toPath());
            } catch (IOException e) {
                throw new AttachmentException("Unable to create directory " + targetDir.getAbsolutePath(), e);
            }
        }
    }

    private String generateReportAttachmentsPath(File attachmentsDir, AbstractReport report) {
        return attachmentsDir.getAbsolutePath() + File.separator + report.getFileNumber() + File.separator +
                report.getKey();
    }

    private void ensureUnique(File file) {
        if (file.exists()) {
            throw new AttachmentException("Attachment file " + file.getName() + " already exists.");
        }
    }

    private void addResourceReference(AbstractReport report, String fileName) {
        final Resource resource = new Resource();
        resource.setReference(fileName);
        resource.setTypes(Collections.singleton(Vocabulary.s_c_SensoryData));
        report.addReference(resource);
        reportService.update(report);
    }

    /**
     * Retrieves the attachment file with the specified name related to the specified report.
     *
     * @param report   Report to which the file is attached
     * @param fileName Attachment name
     * @return Attachment file
     * @throws AttachmentException If the attachment does not exist
     */
    public File getAttachment(AbstractReport report, String fileName) {
        Objects.requireNonNull(report);
        Objects.requireNonNull(fileName);

        final String attachmentPath = generateReportAttachmentsPath(getAttachmentsDir(), report);
        final File result = new File(attachmentPath + File.separator + fileName);
        if (!result.exists()) {
            throw new NotFoundException("Attachment file " + fileName + " not found for report " + report + ".");
        }
        return result;
    }

    /**
     * Deletes attachment file represented by the specified resource.
     *
     * @param report   Report to which the resource is attached
     * @param resource Resource representing the attachment file
     */
    public void deleteAttachment(AbstractReport report, Resource resource) {
        Objects.requireNonNull(report);
        Objects.requireNonNull(resource);

        final File attachment = new File(
                generateReportAttachmentsPath(getAttachmentsDir(), report) + File.separator + resource.getReference());
        if (!attachment.exists()) {
            return;
        }
        boolean result = attachment.delete();
        if (result) {
            LOG.debug("Successfully deleted attachment {} of report {}.", resource.getReference(), report);
        } else {
            LOG.warn("Failed to delete attachment {} of report {}.", resource.getReference(), report);
        }
    }

    /**
     * Deletes all attachment files related to the specified report.
     *
     * @param report Report whose attachments should be removed
     */
    public void deleteAttachments(AbstractReport report) {
        Objects.requireNonNull(report);
        final File reportDir = new File(generateReportAttachmentsPath(getAttachmentsDir(), report));
        if (!reportDir.exists()) {
            return;
        }
        LOG.debug("Deleting all attachments of report {}.", report);
        recursivelyDeleteDirectory(reportDir);
    }

    private void recursivelyDeleteDirectory(File directory) {
        try {
            Files.walk(directory.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            throw new AttachmentException("Unable to delete attachment directory " + directory, e);
        }
    }

    /**
     * Deletes all attachments related to reports in a chain with the specified file number.
     *
     * @param fileNumber Report chain identifier
     */
    public void deleteAttachments(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final File chainDir = new File(getAttachmentsDir().getAbsolutePath() + File.separator + fileNumber);
        if (!chainDir.exists()) {
            return;
        }
        LOG.debug("Deleting all attachments for report chain with file number {}.", fileNumber);
        recursivelyDeleteDirectory(chainDir);
    }
}
