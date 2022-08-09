package com.registration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

/**
 * User api rest handler controller
 * intercept thrown errors and return ErrorMessage with proper HttpStatus
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Handle ResourceNotFoundException
     * return ErrorMessage with HttpStatus NOT_FOUND
     *
     * @param ResourceNotFoundException ex
     * @return ErrorMessage
     */
    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(),
            new Date(),
            ex.getMessage(),
            "Resource Not Found");
    }

    /**
     * Handle IllegalArgumentException
     * return ErrorMessage with HttpStatus BAD_REQUEST
     *
     * @param IllegalArgumentException ex
     * @return ErrorMessage
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage dataNotAllowedException(IllegalArgumentException ex) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
            new Date(),
            ex.getMessage(),
            "Argument not allowed");
    }
}
