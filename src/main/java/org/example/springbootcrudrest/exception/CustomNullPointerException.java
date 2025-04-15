package org.example.springbootcrudrest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomNullPointerException extends BaseException {

    public CustomNullPointerException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
