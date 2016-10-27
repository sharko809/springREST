package com.serviceapp.exception;

/**
 * Thrown during token generation if provided password and one stored in database don't match
 */
public class PasswordMismatchException extends Exception {

    public PasswordMismatchException(String message) {
        super(message);
    }
}
