package cz.cvut.kbss.inbas.audit.rest.exceptions;

/**
 * Thrown when portal-based authentication fails.
 */
public class PortalAuthenticationException extends RuntimeException {

    public PortalAuthenticationException() {
    }

    public PortalAuthenticationException(String message) {
        super(message);
    }

    public PortalAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PortalAuthenticationException(Throwable cause) {
        super(cause);
    }
}
