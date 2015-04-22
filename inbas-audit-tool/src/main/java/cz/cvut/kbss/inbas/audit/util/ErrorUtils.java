package cz.cvut.kbss.inbas.audit.util;

/**
 * Error utilities.
 */
public class ErrorUtils {

    private ErrorUtils() {
        throw new AssertionError();
    }

    /**
     * Generates a message for NullPointerException thrown when checking method parameters.
     *
     * @param paramName Parameter name
     * @return Exception message
     */
    public static String createNPXMessage(String paramName) {
        return "Parameter " + paramName + " cannot be null.";
    }
}
