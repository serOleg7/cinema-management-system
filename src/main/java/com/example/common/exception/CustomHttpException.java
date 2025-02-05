package com.example.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomHttpException extends RuntimeException {

  private final HttpStatus status;

  public CustomHttpException(String message) {
    this(HttpStatus.BAD_REQUEST, message);
  }

  public CustomHttpException(HttpStatus status, String message) {
    super(message);

    this.status = status;
  }

}
