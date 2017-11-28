package cz.cvut.kbss.reporting.exception;

/**
 * Thrown when report import fails.
 */
public class ReportImportingException extends ReportingToolException {

    public ReportImportingException(String message) {
        super(message);
    }

    public ReportImportingException(String message, Throwable cause) {
        super(message, cause);
    }
}
