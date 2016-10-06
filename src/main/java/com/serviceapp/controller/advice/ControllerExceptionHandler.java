package com.serviceapp.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles exceptions caught in controllers
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> invalidArgument(HttpServletRequest request, Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "400");
        errorResponse.put("message", "Invalid query param");
        errorResponse.put("error message", ex.getMessage());
        errorResponse.put("query", request.getQueryString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

//    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested url is not found on this resource, sorry :(")
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> invalidUrl() {
        return new ResponseEntity<>("Requested url is not found on this resource, sorry :(", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    public void getException(Throwable throwable) {
        System.out.println("Throwable class: " + throwable.getClass());
    }

}