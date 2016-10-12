package com.serviceapp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Implementation of Spring Security userdetails <code>User</code> class. Models core user information retrieved
 * by a <code>UserDetailsService</code>
 */
public class UserDetailsImpl extends User {

    private Long id;
    private String userName;
    private String login;
    private Collection<GrantedAuthority> authorities;
    private boolean banned;

    public UserDetailsImpl(Long id, String username, String login, String password,
                           Collection<? extends GrantedAuthority> authorities, Boolean banned) {
        super(login, password, authorities);
        this.id = id;
        this.userName = username;
        this.login = login;
        this.authorities = (Collection<GrantedAuthority>) authorities;
        this.banned = banned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
