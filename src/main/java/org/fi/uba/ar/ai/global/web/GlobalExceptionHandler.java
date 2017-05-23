package org.fi.uba.ar.ai.global.web;

import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class handles exceptions raised in any part of the application
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseBody
  public ResponseEntity<?> handleEntityNotFound(final Exception ex) {
    return logAndReturnResponseEntityForError(ex, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
  @ResponseBody
  public ResponseEntity<?> handleBadRequests(final Exception ex) {
    return logAndReturnResponseEntityForError(ex, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<?> logAndReturnResponseEntityForError(final Exception ex,
      final HttpStatus httpStatus) {
    logger.error(ex.getMessage(), ex.getCause());
    HttpApiError error = new HttpApiError(httpStatus, ex.getMessage());
    return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
  }
}