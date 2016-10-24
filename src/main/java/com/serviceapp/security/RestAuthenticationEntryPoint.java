package com.serviceapp.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used to commence authentication scheme. Well, in case of this application it should just signal about unauthorized
 * request
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        LOGGER.warn("Exception while auth: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access attempt");
    }

}
