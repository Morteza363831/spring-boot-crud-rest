package org.example.springbootcrudrest.utility;

import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class Validator {

    private final jakarta.validation.Validator validator;

    public void validate(Object o) {
        final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(o);

        constraintViolations.forEach(constraintViolation -> log.error(constraintViolation.getMessage()));

        // TODO : EXCEPTION HANDLING
    }
}
