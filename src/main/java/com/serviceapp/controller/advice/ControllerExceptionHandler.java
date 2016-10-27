package com.serviceapp.controller.advice;

import com.serviceapp.util.ResponseErrorHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles exceptions caught in controllers
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity invalidArgument(HttpServletRequest request, Exception ex) {
        LOGGER.warn("Invalid url parameters detected", ex);
        return ResponseErrorHelper.responseError(HttpStatus.BAD_REQUEST, "Invalid url param", ex, request);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity invalidUrl(HttpServletRequest request) {
        LOGGER.warn("Invalid url access attempt: {}",
                request.getRequestURI() + (request.getQueryString() == null ? "" : request.getQueryString()));
        return ResponseErrorHelper
                .responseError(HttpStatus.NOT_FOUND, "Requested url is not found on this resource, sorry :(");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity methodNotSupported(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        LOGGER.warn("Unsupported method request for url: {}, method {}",
                request.getRequestURI() + (request.getQueryString() == null ? "" : request.getQueryString()),
                request.getMethod());
        return ResponseErrorHelper.responseError(HttpStatus.METHOD_NOT_ALLOWED, "Unsupported method", ex);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity mediaTypeNotSupported(HttpServletRequest request, HttpMediaTypeNotSupportedException ex) {
        LOGGER.warn("Unsupported media type for url: {}, content type {}",
                request.getRequestURI() + (request.getQueryString() == null ? "" : request.getQueryString()),
                request.getContentType());
        return ResponseErrorHelper.responseError(HttpStatus.NOT_ACCEPTABLE, "Unsupported method", ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity notReadable(HttpMessageNotReadableException ex) {
        LOGGER.warn("Couldn't read document", ex);
        return ResponseErrorHelper.responseError(HttpStatus.BAD_REQUEST, "Failed to read data", ex);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity getException(HttpServletRequest request, Exception ex) {
        LOGGER.error("Something bad happened", ex);
        String message = (ex.getMessage() == null) ? "Something bad happened" : ex.getMessage();
        return ResponseErrorHelper.responseError(HttpStatus.BAD_REQUEST, message, ex, request);
    }

}
