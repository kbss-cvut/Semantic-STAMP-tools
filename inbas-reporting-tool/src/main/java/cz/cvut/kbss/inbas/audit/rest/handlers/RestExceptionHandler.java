package cz.cvut.kbss.inbas.audit.rest.handlers;

import cz.cvut.kbss.inbas.audit.exceptions.InvalidReportException;
import cz.cvut.kbss.inbas.audit.exceptions.UsernameExistsException;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains exception handlers for REST controllers.
 *
 * @author ledvima1
 */
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> resourceNotFound(HttpServletRequest request, NotFoundException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.NOT_FOUND);
    }

    private ErrorInfo errorInfo(HttpServletRequest request, Throwable e) {
        return new ErrorInfo(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidReportException.class)
    public ResponseEntity<ErrorInfo> invalidReport(HttpServletRequest request, InvalidReportException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorInfo> mappingException(HttpServletRequest request, HttpMessageNotReadableException e) {
        return new ResponseEntity<>(errorInfo(request, e.getCause()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<ErrorInfo> usernameExistsException(HttpServletRequest request, UsernameExistsException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }
}
