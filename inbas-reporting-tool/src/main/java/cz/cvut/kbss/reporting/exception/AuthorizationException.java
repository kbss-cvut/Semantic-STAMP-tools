package cz.cvut.kbss.reporting.exception;

/**
 * Thrown when access to a resource or functionality invocation is denied due to insufficient permission.
 */
public class AuthorizationException extends ReportingToolException {

    public AuthorizationException(String message) {
        super(message);
    }
}
