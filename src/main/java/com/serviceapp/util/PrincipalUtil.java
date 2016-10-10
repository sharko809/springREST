package com.serviceapp.util;

import com.serviceapp.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Helper class to retrieve current user principal
 */
public class PrincipalUtil {

    public static UserDetailsImpl getCurrentPrincipal() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}