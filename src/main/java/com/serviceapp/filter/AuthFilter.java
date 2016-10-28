package com.serviceapp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviceapp.entity.ErrorEntity;
import com.serviceapp.util.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication filter. Helps customize login handling
 */
public class AuthFilter extends BasicAuthenticationFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public AuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException failed) throws IOException {
        super.onUnsuccessfulAuthentication(request, response, failed);
        if (failed instanceof BadCredentialsException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ResponseHelper.setCorsHeader(response);
            OBJECT_MAPPER
                    .writeValue(response.getWriter(), new ErrorEntity(HttpStatus.UNAUTHORIZED, "Wrong password or username"));
            new SecurityContextLogoutHandler().logout(request, response, null);
        }
    }

}
