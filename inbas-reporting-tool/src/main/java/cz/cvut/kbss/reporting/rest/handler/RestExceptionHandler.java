package cz.cvut.kbss.reporting.rest.handler;

import cz.cvut.kbss.jopa.exceptions.OWLPersistenceException;
import cz.cvut.kbss.reporting.exception.*;
import cz.cvut.kbss.reporting.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains exception handlers for REST controllers.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    private void logException(RuntimeException ex) {
        logException("Exception caught.", ex);
    }

    private void logException(String message, RuntimeException ex) {
        LOG.error(message, ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> resourceNotFound(HttpServletRequest request, NotFoundException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.NOT_FOUND);
    }

    private ErrorInfo errorInfo(HttpServletRequest request, Throwable e) {
        return new ErrorInfo(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorInfo> invalidReport(HttpServletRequest request, ValidationException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorInfo> mappingException(HttpServletRequest request, HttpMessageNotReadableException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e.getCause() != null ? e.getCause() : e),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<ErrorInfo> usernameExistsException(HttpServletRequest request, UsernameExistsException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WebServiceIntegrationException.class)
    public ResponseEntity<ErrorInfo> webServiceIntegrationException(HttpServletRequest request,
                                                                    WebServiceIntegrationException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorInfo> unsupportedOperationException(HttpServletRequest request,
                                                                   UnsupportedOperationException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInfo> illegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorInfo> persistenceException(HttpServletRequest request, PersistenceException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e.getCause()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OWLPersistenceException.class)
    public ResponseEntity<ErrorInfo> jopaException(HttpServletRequest request, OWLPersistenceException e) {
        logException("Persistence exception caught.", e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReportImportingException.class)
    public ResponseEntity<ErrorInfo> reportImportingException(HttpServletRequest request, ReportImportingException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedReportTypeException.class)
    public ResponseEntity<ErrorInfo> unsupportedReportTypeException(HttpServletRequest request,
                                                                    UnsupportedReportTypeException e) {
        logException(e);
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.BAD_REQUEST);
    }
}
