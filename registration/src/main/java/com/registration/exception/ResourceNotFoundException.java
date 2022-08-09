package com.registration.exception;

/**
 * Resource not found exception
 * When entity not found
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
