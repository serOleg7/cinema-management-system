package com.example.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Data;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiException {

  public static final String DEFAULT_ERROR_MESSAGE = "Internal error";

  private HttpStatus status;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;

  private String message;
  private String exception;
  private String stackTrace;
  private String path;

  private ApiException(HttpStatus status, String message, String exception, String stackTrace, String path) {
    this.status = status;
    this.timestamp = LocalDateTime.now();
    this.message = message;
    this.exception = exception;
    this.stackTrace = stackTrace;
    this.path = path;
  }

  public static ApiException withoutStackTrace(HttpStatus status, String message, String path) {
    return new ApiException(status, message, null, null, path);
  }

  public static ApiException withStackTrace(HttpStatus status, String message, Throwable ex, String path) {
    return new ApiException(
        status,
        message,
        ex.getClass().getName(),
        ExceptionUtils.getStackTrace(ex),
        path
    );
  }

}
