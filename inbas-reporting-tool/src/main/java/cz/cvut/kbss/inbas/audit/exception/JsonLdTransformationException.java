package cz.cvut.kbss.inbas.audit.exception;

/**
 * Thrown when an exception occurs during processing of JSON-LD input.
 */
public class JsonLdTransformationException extends RuntimeException {

    public JsonLdTransformationException() {
    }

    public JsonLdTransformationException(String message) {
        super(message);
    }

    public JsonLdTransformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonLdTransformationException(Throwable cause) {
        super(cause);
    }
}
