package cz.cvut.kbss.inbas.reporting.rest.exception;

/**
 * Generic exception for bad requests.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
