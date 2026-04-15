package com.java_api.exception;

import com.java_api.exception.custom.UserEmailAlreadyTakenException;
import com.java_api.exception.custom.WrongCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest wr) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                errors,
                wr.getDescription(false)
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableExceptions(HttpMessageNotReadableException ex, WebRequest wr) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                Collections.singletonList("Invalid request body"),
                wr.getDescription(false)
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserEmailAlreadyTakenException.class)
    public ResponseEntity<ExceptionResponse> handleInternalExceptions(UserEmailAlreadyTakenException ex, WebRequest wr) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                Collections.singletonList(ex.getMessage()),
                wr.getDescription(false)
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleWrongCredentialsExceptions(WrongCredentialsException ex, WebRequest wr) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                Collections.singletonList(ex.getMessage()),
                wr.getDescription(false)
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleInternalExceptions(Exception ex, WebRequest wr) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                Collections.singletonList(ex.getMessage()),
                wr.getDescription(false)
        );

        return ResponseEntity.internalServerError().body(response);
    }
}
