package cz.cvut.kbss.inbas.audit.exception;

/**
 * Thrown when the application initialization fails for some reason.
 */
public class ApplicationInitializationException extends RuntimeException {

    public ApplicationInitializationException(String message) {
        super(message);
    }

    public ApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationInitializationException(Throwable cause) {
        super(cause);
    }
}
