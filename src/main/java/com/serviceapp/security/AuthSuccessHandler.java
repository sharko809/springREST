package com.serviceapp.security;

import com.serviceapp.entity.ErrorEntity;
import com.serviceapp.util.PrincipalUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Class implements <code>AuthenticationSuccessHandler</code>. Single public overridden method
 * <code>onAuthenticationSuccess</code> determines URL which user will be redirected to after successful
 * authentication. Also handles banned users.
 */
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    /**
     * Handles redirect strategy based upon user role. Or if user is banned - removes authentication.
     *
     * @param request        - the request which caused the successful authentication
     * @param response       - the response
     * @param authentication - the Authentication object which was created during the authentication process.
     * @throws IOException in the event of an IOException
     */
    private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        UserDetailsImpl currentPrincipal = PrincipalUtil.getCurrentPrincipal();
        if (currentPrincipal == null || currentPrincipal.isBanned()) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            ErrorEntity error = new ErrorEntity(HttpStatus.FORBIDDEN, "You are banned");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(error.toJsonString());
        }

        if (request.getParameter("loginPage") != null) {
            String redirect = makeTargetUrl(authentication);
            LOGGER.debug("Redirecting to: {}", redirect);
            response.sendRedirect(makeTargetUrl(authentication));
        }

    }

    /**
     * Build target URL depending on user role.
     *
     * @param authentication user auth details
     * @return String redirect URL based on user role
     */
    private String makeTargetUrl(Authentication authentication) {
        boolean user = false;
        boolean admin = false;

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                admin = true;
                break;
            } else if ("ROLE_USER".equals(authority.getAuthority())) {
                user = true;
                break;
            }
        }

        if (user) {
            return "/movies";
        } else if (admin) {
            return "/admin";
        } else {
            LOGGER.warn("User with no role detected. {}", authentication.getCredentials());
            throw new IllegalStateException("User with no role detected. " + authentication.getCredentials());
        }
    }

    /**
     * Removes authentication exceptions from session if exists.
     *
     * @param request HttpServletRequest
     */
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
