package com.serviceapp.util;

import com.serviceapp.entity.ErrorEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Helper class for convenience when creating error response
 */
public class ResponseErrorHelper {

    /**
     * Creates a <code>ResponseEntity</code> object provided with error data
     *
     * @param httpStatus  http status
     * @param userMessage message dedicated to user
     * @return <code>ResponseEntity</code> populated with error data
     */
    public static ResponseEntity responseError(HttpStatus httpStatus, String userMessage) {
        ErrorEntity error = new ErrorEntity(httpStatus, userMessage);
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Creates a <code>ResponseEntity</code> object provided with error data
     *
     * @param httpStatus   http status
     * @param userMessages list of messages dedicated to user
     * @return <code>ResponseEntity</code> populated with error data
     */
    public static ResponseEntity responseError(HttpStatus httpStatus, List<String> userMessages) {
        ErrorEntity error = new ErrorEntity(httpStatus, userMessages);
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Creates a <code>ResponseEntity</code> object provided with error data
     *
     * @param httpStatus  http status
     * @param userMessage message dedicated to user
     * @param throwable   throwable that caused an exception
     * @return <code>ResponseEntity</code> populated with error data
     */
    public static ResponseEntity responseError(HttpStatus httpStatus, String userMessage, Throwable throwable) {
        ErrorEntity error = new ErrorEntity(httpStatus, userMessage, throwable);
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Creates a <code>ResponseEntity</code> object provided with error data
     *
     * @param httpStatus  http status
     * @param userMessage message dedicated to user
     * @param throwable   throwable that caused an exception
     * @param request     request that caused an exception
     * @return <code>ResponseEntity</code> populated with error data
     */
    public static ResponseEntity responseError(HttpStatus httpStatus, String userMessage, Throwable throwable,
                                               HttpServletRequest request) {
        ErrorEntity error = new ErrorEntity(httpStatus, userMessage, throwable, request);
        return new ResponseEntity<>(error, error.getStatus());
    }

}
