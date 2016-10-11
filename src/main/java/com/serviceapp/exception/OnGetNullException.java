package com.serviceapp.exception;

/**
 * This exception is thrown when SQL, Hibernate or Database exception occur during reading from database
 */
public class OnGetNullException extends Exception {

    public OnGetNullException() {
        super();
    }

    public OnGetNullException(String message) {
        super(message);
    }

    public OnGetNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public OnGetNullException(Throwable cause) {
        super(cause);
    }

}
