package com.furnitureshop.exception;

/** Thrown when user input or a business rule is not valid. */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
