package com.serviceapp.security;

import com.serviceapp.security.securityEntity.TokenAuthentication;
import com.serviceapp.service.CustomTokenService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

/**
 * Authentication manager implementation used to provide authentication to protected resources via JWT
 */
@Service
public class TokenAuthenticationManager implements AuthenticationManager {

    private CustomTokenService tokenService;

    @Autowired
    public TokenAuthenticationManager(CustomTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication instanceof TokenAuthentication) {
            return processAuthentication((TokenAuthentication) authentication);
        } else {
            authentication.setAuthenticated(false);
            return authentication;
        }

    }

    /**
     * Processes token data and checks it for validity
     *
     * @param authentication <code>TokenAuthentication</code> object
     * @return <code>TokenAuthentication</code> object populated with full authentication data if token is valid
     * @throws AuthenticationException thrown if token is invalid or expiration date has passed
     */
    private TokenAuthentication processAuthentication(TokenAuthentication authentication) throws AuthenticationException {
        String token = authentication.getToken();

        DefaultClaims claims;
        try {
            claims = (DefaultClaims) tokenService.parseToken(token);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Token corrupted");
        }

        if (claims.get("token_expiration_date", Long.class) == null) {
            throw new AuthenticationServiceException("Invalid token");
        }

        Date expirationDate = new Date(claims.get("token_expiration_date", Long.class));
        if (expirationDate.after(new Date())) {
            return makeAuthentication(authentication, claims);
        } else {
            throw new AuthenticationServiceException("Token expired");
        }

    }

    /**
     * Completes authentication object populating it with full authentication data.
     *
     * @param authentication <code>TokenAuthentication</code> object containing token
     * @param claims         claims from the token
     * @return full authentication object
     * @throws AuthenticationException thrown if user is banned (somehow...)
     */
    private TokenAuthentication makeAuthentication(TokenAuthentication authentication, DefaultClaims claims)
            throws AuthenticationException {
        boolean banned = claims.get("user_banned", Boolean.class);
        if (!banned) {
            Collection<GrantedAuthority> authorities = claims.get("user_authorities", Collection.class);
            String login = claims.get("user_login", String.class);
            String userName = claims.get("user_name", String.class);
            return new TokenAuthentication(authentication.getToken(), authorities, true, login, userName);
        } else {
            throw new AuthenticationServiceException("User is banned");
        }
    }

}
