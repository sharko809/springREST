package com.serviceapp.security;

import com.serviceapp.entity.ErrorEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Class for handling strategy when user attempts to access forbidden resource
 */
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
            throws IOException, ServletException {
        handleDenied(request, response, exception);
        clearAuthenticationAttributes(request);
    }

    /**
     * Handles access denial
     *
     * @param request  - that resulted in an AccessDeniedException
     * @param response - so that the user agent can be advised of the failure
     * @param ex       - that caused the invocation
     * @throws IOException in the event of an IOException
     */
    private void handleDenied(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String message = "Action is forbidden" +
                ("/".equals(request.getServletPath()) ? ": authorized users can't access login page" : "");
        response.getWriter().write(new ErrorEntity(HttpStatus.FORBIDDEN, message, ex).toJsonString());
    }

    /**
     * Removes authentication exceptions from session if exists.
     *
     * @param request HttpServletRequest
     */
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
