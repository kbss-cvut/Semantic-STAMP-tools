package cz.cvut.kbss.reporting.exception;

public class FileFormatNotSupportedException extends ReportingToolException {

    public FileFormatNotSupportedException(String message) {
        super(message);
    }

    public FileFormatNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileFormatNotSupportedException(Throwable cause) {
        super(cause);
    }
}
