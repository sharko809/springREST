package com.serviceapp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviceapp.entity.ErrorEntity;
import com.serviceapp.security.UserDetailsImpl;
import com.serviceapp.util.PrincipalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter handling basic authentication actions like failure and success handlers
 */
public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public CustomBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              Authentication authentication) throws IOException {
        UserDetailsImpl currentPrincipal = PrincipalUtil.getCurrentPrincipal();
        if (currentPrincipal != null) {
            if (currentPrincipal.isBanned()) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                OBJECT_MAPPER.writeValue(response.getWriter(), new ErrorEntity(HttpStatus.FORBIDDEN, "You are banned"));
            }
        } else {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            OBJECT_MAPPER
                    .writeValue(response.getWriter(), new ErrorEntity(HttpStatus.FORBIDDEN, "Corrupted authentication"));
        }
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException failed) throws IOException {
        System.out.println("UNSUCCESS");
        super.onUnsuccessfulAuthentication(request, response, failed);
        // TODO

    }
}
