package cz.cvut.kbss.reporting.exception;

/**
 * Parent for all exceptions in this applications.
 */
public class ReportingToolException extends RuntimeException {

    public ReportingToolException(String message) {
        super(message);
    }

    public ReportingToolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportingToolException(Throwable cause) {
        super(cause);
    }
}
