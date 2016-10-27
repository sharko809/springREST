package com.serviceapp.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown if authentication token is missing or corrupted
 */
public class TokenMissingException extends AuthenticationException {

    public TokenMissingException(String msg, Throwable t) {
        super(msg, t);
    }

    public TokenMissingException(String msg) {
        super(msg);
    }
}
