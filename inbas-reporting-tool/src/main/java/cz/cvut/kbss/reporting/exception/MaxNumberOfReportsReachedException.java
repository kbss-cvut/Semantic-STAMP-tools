package cz.cvut.kbss.reporting.exception;

/**
 * Parent for all exceptions in this applications.
 */
public class MaxNumberOfReportsReachedException extends RuntimeException {

    public MaxNumberOfReportsReachedException(String message) {
        super(message);
    }

    public MaxNumberOfReportsReachedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxNumberOfReportsReachedException(Throwable cause) {
        super(cause);
    }
}
