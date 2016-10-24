package com.serviceapp.security;

import com.serviceapp.entity.User;
import com.serviceapp.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom authentication provider for Spring Security to authenticate users
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LogManager.getLogger();
    private UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Attempts to get user details
     *
     * @param login login of user to look for
     * @return <code>UserDetails</code> object with all user specific info (same as in <code>User</code> class)
     * @throws UsernameNotFoundException thrown when user with provided login is not found in database
     * @see User
     */
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        LOGGER.info("Trying to login {}", login);// TODO handle writer error
        User user = userService.getUserByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User with login " + login + " not found.");
        }
        LOGGER.info("Found {}", user.getName());

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!user.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new UserDetailsImpl(
                user.getId(), user.getName(), user.getLogin(), user.getPassword(), authorities, user.isBanned());
    }

}