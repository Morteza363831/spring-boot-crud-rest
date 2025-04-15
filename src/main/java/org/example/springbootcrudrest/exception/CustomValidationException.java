package org.example.springbootcrudrest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomValidationException extends BaseException {
    private final String errors;
    public CustomValidationException(List<String> errors) {
        super("Validation Error : " + errors.toString() , HttpStatus.BAD_REQUEST);
        this.errors = "Validation Error : " + errors;
    }
}