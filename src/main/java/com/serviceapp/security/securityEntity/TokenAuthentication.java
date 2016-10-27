package com.serviceapp.security.securityEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Custom implementation of spring's <code>Authentication</code> interface. Serves as an authentication object during
 * token authentication.
 */
public class TokenAuthentication implements Authentication {

    private String token;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAuthenticated;
    private UserDetailsImpl principal;

    public TokenAuthentication(String token) {
        this.token = token;
    }

    public TokenAuthentication(String token, Collection<? extends GrantedAuthority> authorities,
                               boolean isAuthenticated, UserDetailsImpl principal) {
        this.token = token;
        this.authorities = authorities;
        this.isAuthenticated = isAuthenticated;
        this.principal = principal;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        if (principal != null) {
            return principal.getUserName();
        }
        return null;
    }
}
