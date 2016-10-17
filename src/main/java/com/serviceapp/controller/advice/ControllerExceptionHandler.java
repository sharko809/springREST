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
        LOGGER.warn("Invalid url parameters detected", ex);
        ErrorEntity errorEntity = new ErrorEntity(HttpStatus.BAD_REQUEST, "Invalid url param", ex, request);
        return new ResponseEntity<>(errorEntity, errorEntity.getStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity invalidUrl(HttpServletRequest request) {
        LOGGER.warn("Invalid url access attempt: {}",
                request.getRequestURI() + (request.getQueryString() == null ? "" : request.getQueryString()));
        ErrorEntity errorEntity =
                new ErrorEntity(HttpStatus.NOT_FOUND, "Requested url is not found on this resource, sorry :(");
        return new ResponseEntity<>(errorEntity, errorEntity.getStatus());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorEntity> getException(HttpServletRequest request, Exception ex) {
        LOGGER.error("Something bad happened", ex);
        ErrorEntity errorEntity = new ErrorEntity(HttpStatus.BAD_REQUEST, "Something bad happened", ex, request);
        return new ResponseEntity<>(errorEntity, errorEntity.getStatus());
    }

}
