package com.serviceapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviceapp.entity.ErrorEntity;
import com.serviceapp.util.ResponseHelper;
import org.springframework.http.HttpStatus;
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
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
        ResponseHelper.setCorsHeader(response);
        OBJECT_MAPPER.writeValue(response.getWriter(),
                new ErrorEntity(HttpStatus.UNAUTHORIZED, authException.getMessage(), authException));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(REALM);
        super.afterPropertiesSet();
    }

}
