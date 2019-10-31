package cz.cvut.kbss.reporting.exception;

/**
 * Signifies that an error occurred when processing RT schema.
 * <p>
 * This could been e.g. that the schema file could not be saved.
 */
public class SchemaException extends ReportingToolException  {
    public SchemaException(String message) {
        super(message);
    }

    public SchemaException(String message, Throwable cause) {
        super(message, cause);
    }
}
