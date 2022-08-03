package com.registration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(),
            new Date(),
            ex.getMessage(),
            "Resource Not Found");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage dataNotAllowedException(IllegalArgumentException ex) {
        return new ErrorMessage(HttpStatus.FORBIDDEN.value(),
            new Date(),
            ex.getMessage(),
            "Argument not allowed");
    }
}
