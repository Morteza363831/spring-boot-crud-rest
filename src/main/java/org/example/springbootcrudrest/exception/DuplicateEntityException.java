package org.example.springbootcrudrest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEntityException extends BaseException {

    public DuplicateEntityException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
