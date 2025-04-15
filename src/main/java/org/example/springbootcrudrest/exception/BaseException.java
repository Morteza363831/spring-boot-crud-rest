package org.example.springbootcrudrest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
  private final String message;
  private final HttpStatus status;
    public BaseException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
