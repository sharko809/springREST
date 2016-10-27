package com.serviceapp.util;

import com.serviceapp.security.securityEntity.UserDetailsImpl;
import org.springframework.security.core.Authentication;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return (UserDetailsImpl) authentication.getPrincipal();
    }

}