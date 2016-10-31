package com.serviceapp.service;

import com.serviceapp.exception.PasswordMismatchException;
import com.serviceapp.security.PasswordManager;
import com.serviceapp.security.securityEntity.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for managing authentication token
 */
@Service
public class CustomTokenService {

    private static final String KEY = "my_d_art_project_key_228";
    private UserDetailsService userDetailsService;
    private PasswordManager passwordManager;

    @Autowired
    public CustomTokenService(UserDetailsService userDetailsService, PasswordManager passwordManager) {
        this.userDetailsService = userDetailsService;
        this.passwordManager = passwordManager;
    }

    /**
     * Parses token for data
     *
     * @param token token to parse
     * @return jwt claims set - json map with decrypted values
     * @throws JwtException thrown if decryption failed
     */
    public Claims parseToken(String token) throws JwtException {
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Generates authentication token based on user login and password
     *
     * @param login    user login
     * @param password user password
     * @return user authentication token
     * @throws PasswordMismatchException thrown in case of password check failure
     */
    public String getToken(String login, String password) throws PasswordMismatchException {
        if (login == null || password == null) {
            return null;
        }

        UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(login);
        if (!passwordManager.matches(password, user.getPassword())) {
            throw new PasswordMismatchException("Authentication error");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(calendar.getTime());
        jwtBuilder.setClaims(completeTokenData(user, calendar));

        return jwtBuilder.signWith(SignatureAlgorithm.HS512, KEY).compact();
    }

    /**
     * Parses user data to <code>Map</code> which serves as token data (claims).
     *
     * @param userDetails user data
     * @param calendar    calendar object to retrieve expiration time
     * @return map with complete token data
     */
    private Map<String, Object> completeTokenData(UserDetails userDetails, Calendar calendar) {
        Map<String, Object> tokenData = new HashMap<>();
        UserDetailsImpl user = (UserDetailsImpl) userDetails;

        tokenData.put("user_id", user.getId());
        tokenData.put("user_login", user.getLogin());
        tokenData.put("user_name", user.getUserName());
        tokenData.put("user_banned", user.isBanned());
        tokenData.put("user_authorities", user.getAuthorities());

        tokenData.put("token_creation_date", new Date().getTime());
        tokenData.put("token_expiration_date", calendar.getTime());

        return tokenData;
    }

}
