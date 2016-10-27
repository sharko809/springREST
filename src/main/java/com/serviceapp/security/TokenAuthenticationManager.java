package com.serviceapp.security;

import com.serviceapp.security.securityEntity.TokenAuthentication;
import com.serviceapp.security.securityEntity.UserDetailsImpl;
import com.serviceapp.service.CustomTokenService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            List authoritiesList = claims.get("user_authorities", List.class);
            authoritiesList.forEach(a -> {
                authorities.add(new SimpleGrantedAuthority(a.toString()));
            });
            Integer id = claims.get("user_id", Integer.class);
            String login = claims.get("user_login", String.class);
            String userName = claims.get("user_name", String.class);
            UserDetailsImpl principal = new UserDetailsImpl(Long.valueOf(id), userName, login, "randomtext", authorities, false);
            return new TokenAuthentication(authentication.getToken(), authorities, true, principal);
        } else {
            throw new AuthenticationServiceException("User is banned");
        }
    }

}
