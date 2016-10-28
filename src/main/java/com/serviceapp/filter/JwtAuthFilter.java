package com.serviceapp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviceapp.entity.ErrorEntity;
import com.serviceapp.exception.TokenMissingException;
import com.serviceapp.security.securityEntity.TokenAuthentication;
import com.serviceapp.util.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to control access to protected resources via authentication token
 */
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public JwtAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager) {
        super(requiresAuthenticationRequestMatcher);
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getRequestDispatcher(request.getServletPath() +
                    (request.getPathInfo() != null ? request.getPathInfo() : "")).forward(request, response);
        });
        setAuthenticationFailureHandler((request, response, authenticationException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ResponseHelper.setCorsHeader(response);
            OBJECT_MAPPER
                    .writeValue(response.getWriter(), new ErrorEntity(HttpStatus.FORBIDDEN, authenticationException.getMessage()));
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            if (!authHeader.startsWith("Bearer ")) {
                throw new TokenMissingException("Token either missing or invalid");
            }
        } else {
            throw new TokenMissingException("No authorization token found in request");
        }

        String authorizationToken = authHeader.substring(7);
        TokenAuthentication tokenAuthentication = new TokenAuthentication(authorizationToken);

        Authentication authenticated = getAuthenticationManager().authenticate(tokenAuthentication);

        if (new AntPathMatcher().match("/admin/**", request.getServletPath())) {
            if (!ifAdmin(authenticated)) {
                ResponseHelper.setCorsHeader(response);
                OBJECT_MAPPER.writeValue(response.getWriter(), new ErrorEntity(HttpStatus.FORBIDDEN, "Access denied"));
            }
        }

        return authenticated;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    /**
     * Checks if provided authentication object contains admin role
     *
     * @param authentication authentication object
     * @return <code>true</code> if provided authentication object contains "ROLE_ADMIN"
     */
    private boolean ifAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        for (GrantedAuthority a : authentication.getAuthorities()) {
            if (a.toString().contains("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }

}
