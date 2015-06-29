package cz.cvut.kbss.inbas.audit.exceptions;

/**
 * Exception thrown when an invalid event report is encountered.
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
