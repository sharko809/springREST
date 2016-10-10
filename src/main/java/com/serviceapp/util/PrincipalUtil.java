package com.serviceapp.util;

import com.serviceapp.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Helper class to retrieve current user principal
 */
public class PrincipalUtil {

    /**
     * Get current user principal
     *
     * @return the <code>Principal</code> being authenticated or the authenticated principal after authentication.
     */
    public static UserDetailsImpl getCurrentPrincipal() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}