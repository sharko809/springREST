package com.serviceapp.controller.advice;

import com.serviceapp.entity.ErrorEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ErrorEntity> invalidArgument(HttpServletRequest request, Exception ex) {
        ErrorEntity errorEntity = new ErrorEntity(HttpStatus.BAD_REQUEST, "Invalid query param", ex, request);
        return new ResponseEntity<>(errorEntity, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> invalidUrl() {
        return new ResponseEntity<>("Requested url is not found on this resource, sorry :(", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorEntity> getException(HttpServletRequest request, Exception ex) {
        LOGGER.error("Class: " + ex.getClass(), ex);
        ErrorEntity errorEntity = new ErrorEntity(HttpStatus.BAD_REQUEST, "Something bad happened", ex, request);
        return new ResponseEntity<>(errorEntity, HttpStatus.BAD_REQUEST);
    }

}
