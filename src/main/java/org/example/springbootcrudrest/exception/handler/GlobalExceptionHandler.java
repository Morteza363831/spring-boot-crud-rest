package org.example.springbootcrudrest.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootcrudrest.config.ResultApi;
import org.example.springbootcrudrest.exception.BaseException;
import org.example.springbootcrudrest.exception.CustomValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleCustomException(final BaseException e,final HttpServletRequest request) {
        return buildErrorResponse(e, e.getStatus(), request);
    }

    @ExceptionHandler(value = CustomValidationException.class)
    public ResponseEntity<?> handleCustomException(final CustomValidationException e,final HttpServletRequest request) {
        log.error("{}, {}", e.getMessage(), e.getErrors());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ResultApi<>(
                        "fail",
                        e.getErrors(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    public ResponseEntity<?> handleJsonProcessingException(final JsonProcessingException e,final HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<?> handleUnauthorizedException(final HttpClientErrorException.Unauthorized e,final HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(final Exception e,final HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR , request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResultApi<>(
                        "fail",
                        e.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseException(HttpMessageNotReadableException ex) {
        log.error("JSON Parsing Error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON input.");
    }

    private ResponseEntity<?> buildErrorResponse(Exception e, HttpStatus status, HttpServletRequest request) {
        log.error("Error occurred: {}", e.getMessage());
        return ResponseEntity
                .status(status)
                .body(new ResultApi<>(
                        "fail",
                        e.getMessage(),
                        request.getRequestURI()
                ));
    }
}