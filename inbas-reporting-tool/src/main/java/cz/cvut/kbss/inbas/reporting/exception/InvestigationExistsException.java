package cz.cvut.kbss.inbas.reporting.exception;

/**
 * Thrown when an attempt to start investigation in a report chain which is already in investigation phase is made.
 */
public class InvestigationExistsException extends RuntimeException {

    public InvestigationExistsException(String message) {
        super(message);
    }
}
