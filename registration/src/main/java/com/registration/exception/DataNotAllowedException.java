package com.registration.exception;

public class DataNotAllowedException extends RuntimeException {
    public DataNotAllowedException(String message) {
        super(message);
    }
}
