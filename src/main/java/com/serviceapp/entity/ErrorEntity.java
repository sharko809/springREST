package com.serviceapp.entity;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing error information
 */
public class ErrorEntity {

    private HttpStatus status;
    private List<String> userMessage;
    private List<String> errorMessage;
    private String query;

    public ErrorEntity(HttpStatus status, List<String> userMessage) {
        this.status = status;
        this.userMessage = userMessage;
    }

    public ErrorEntity(HttpStatus status, String userMessage) {
        this.status = status;
        this.userMessage = new ArrayList<>();
        this.userMessage.add(userMessage);
    }

    public ErrorEntity(HttpStatus status, List<String> userMessage, List<String> errorMessage) {
        this.status = status;
        this.userMessage = userMessage;
        this.errorMessage = errorMessage;
    }

    public ErrorEntity(HttpStatus status, String userMessage, String errorMessage) {
        this.status = status;
        this.userMessage = new ArrayList<>();
        this.userMessage.add(userMessage);
        this.errorMessage = new ArrayList<>();
        this.errorMessage.add(errorMessage);
    }

    public ErrorEntity(HttpStatus status, List<String> userMessage, Throwable throwable) {
        this.status = status;
        this.userMessage = userMessage;
        this.errorMessage = new ArrayList<>();
        this.errorMessage.add(throwable.getMessage());
    }

    public ErrorEntity(HttpStatus status, String userMessage, Throwable throwable) {
        this.status = status;
        this.userMessage = new ArrayList<>();
        this.userMessage.add(userMessage);
        this.errorMessage = new ArrayList<>();
        this.errorMessage.add(throwable.getMessage());
    }

    public ErrorEntity(HttpStatus status, List<String> userMessage, Throwable throwable, HttpServletRequest request) {
        this.status = status;
        this.userMessage = userMessage;
        this.errorMessage = new ArrayList<>();
        this.errorMessage.add(throwable.getMessage());
        this.query = request.getQueryString();
    }

    public ErrorEntity(HttpStatus status, List<String> userMessage, List<String> errorMessage, HttpServletRequest request) {
        this.status = status;
        this.userMessage = userMessage;
        this.errorMessage = errorMessage;
        this.query = request.getQueryString();
    }

    public ErrorEntity(HttpStatus status, String userMessage, Throwable throwable, HttpServletRequest request) {
        this.status = status;
        this.userMessage = new ArrayList<>();
        this.userMessage.add(userMessage);
        this.errorMessage = new ArrayList<>();
        this.errorMessage.add(throwable.getMessage());
        this.query = request.getQueryString();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public List<String> getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(List<String> userMessage) {
        this.userMessage = userMessage;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "ErrorEntity{" +
                "status=" + status +
                ", userMessage=" + userMessage +
                ", errorMessage=" + errorMessage +
                ", query='" + query + '\'' +
                '}';
    }
}
