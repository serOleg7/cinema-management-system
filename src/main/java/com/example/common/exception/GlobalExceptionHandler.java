package com.example.common.exception;

import com.example.user.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Hidden
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  private final HttpServletRequest request;
  @Value("${detailed-api-error-response}")
  private Boolean dumpStackTrace;

  @ExceptionHandler(CustomHttpException.class)
  public ResponseEntity<Object> handleCustomHttpException(CustomHttpException ex) {
    return buildErrorResponse(ex.getStatus(), ex);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUnhandledException(Exception ex) {
    logger.error("Unhandled exception occurred at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ApiException.DEFAULT_ERROR_MESSAGE, ex);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, ex);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public ResponseEntity<Object> handleSQLIntegrityConstraintViolation(SQLIntegrityConstraintViolationException ex) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler({BadCredentialsException.class, UnauthorizedException.class})
  public ResponseEntity<Object> handleUnauthorizedExceptions(Exception ex) {
    return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request
  ) {
    StringBuilder messageBuilder = new StringBuilder("Validation failed: ");
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      messageBuilder.append(error.getField())
          .append(" - ")
          .append(error.getDefaultMessage())
          .append("; ");
    });
    messageBuilder.setLength(messageBuilder.length() - 2);

    return buildErrorResponse(HttpStatus.BAD_REQUEST, messageBuilder.toString(), ex);
  }

  private ResponseEntity<Object> buildErrorResponse(HttpStatus status, Throwable ex) {
    return buildErrorResponse(status, ex.getMessage(), ex);
  }

  private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message, Throwable ex) {
    ApiException apiError = dumpStackTrace
        ? ApiException.withStackTrace(status, message, ex, request.getRequestURI())
        : ApiException.withoutStackTrace(status, message, request.getRequestURI());

    return new ResponseEntity<>(apiError, status);
  }

}
