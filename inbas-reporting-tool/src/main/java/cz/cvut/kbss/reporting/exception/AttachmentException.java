package cz.cvut.kbss.reporting.exception;

/**
 * Signifies that an error occurred when processing report attachments.
 * <p>
 * This could bean e.g. that the attachment file could not be saved.
 */
public class AttachmentException extends ReportingToolException {

    public AttachmentException(String message) {
        super(message);
    }

    public AttachmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
