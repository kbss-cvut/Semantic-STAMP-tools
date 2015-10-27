package cz.cvut.kbss.inbas.audit.exception;

/**
 * Exception thrown when an invalid occurrence report is encountered.
 *
 * @author ledvima1
 */
public class InvalidReportException extends RuntimeException {

    public InvalidReportException() {
    }

    public InvalidReportException(String message) {
        super(message);
    }

    public InvalidReportException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidReportException(Throwable cause) {
        super(cause);
    }
}
