package com.serviceapp.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used to commence authentication scheme. Well, in case of this application it should just signal about unauthorized
 * request and let auth
 */
public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private static final String REALM = "SH_DART_REALM";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(REALM);
        super.afterPropertiesSet();
    }

}
