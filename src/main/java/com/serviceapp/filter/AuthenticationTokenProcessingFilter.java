package com.serviceapp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviceapp.security.UserToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by dsharko on 10/17/2016.
 */
public class AuthenticationTokenProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String HEADER_SECURITY_TOKEN = "SH-Rest-Token";
    private AuthenticationManager authenticationManager;
//    @Autowired
//    private FilterSecurityInterceptor filterSecurityInterceptor;

    public AuthenticationTokenProcessingFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager,
                                               AuthenticationSuccessHandler authenticationSuccessHandler) {
        super(defaultFilterProcessesUrl);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl, HttpMethod.GET.toString()));
        this.authenticationManager = authenticationManager;
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String token = request.getHeader(HEADER_SECURITY_TOKEN);
        Authentication userAuthToken = parseToken(token);
//        if (filterSecurityInterceptor != null) {
//            filterSecurityInterceptor.obtainSecurityMetadataSource().getAllConfigAttributes().forEach(configAttribute -> System.out.println("atr: " + configAttribute.getAttribute()));
//            try {
//                Field field = filterSecurityInterceptor.obtainSecurityMetadataSource().getClass().getDeclaredField("requestMap");
//                field.setAccessible(true);
////                Map<RequestMatcher, Collection<ConfigAttribute>> map = Collections.map;
//                Object object = field.get(Class.forName(field.getGenericType()));
//                System.out.println(object);
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
        if (userAuthToken == null) {
            throw new AuthenticationServiceException("authorization required to access this resource");
        }
        return userAuthToken;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private Authentication parseToken(String tokenString) {
        if (tokenString == null || tokenString.isEmpty()) {
            return null;
        }
        // decryption here
        String decryptedToken = new String(Base64.getDecoder().decode(tokenString), StandardCharsets.UTF_8);
        LOGGER.debug("Decrypted token: {}", decryptedToken);

        try {
            UserToken token = new ObjectMapper().readValue(decryptedToken, UserToken.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(token.getLogin(), token.getPassword()));
        } catch (IOException e) {
            LOGGER.warn("Exception during authentication: {}", e.getMessage());
            LOGGER.warn(e);
            return null;
        }
    }

}
