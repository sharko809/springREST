package com.serviceapp.controller;

import com.serviceapp.exception.PasswordMismatchException;
import com.serviceapp.service.CustomTokenService;
import com.serviceapp.util.ResponseErrorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Handles login operation. Attempt login method is called to provide user with authentication token
 */
@RestController
@RequestMapping("/loginPage")
public class LoginController {

    private CustomTokenService tokenService;

    @Autowired
    public LoginController(CustomTokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Generates token and returns it to the client
     *
     * @param authorizationHeader header containing authorization data
     * @return <code>ResponseEntity</code> with encrypted user authorization token
     */
    @GetMapping
    public ResponseEntity attemptLogin(@RequestHeader("Authorization") String authorizationHeader) {
        String authorization = authorizationHeader.substring(6);
        String decryptedAuthorizationPart = new String(Base64.getDecoder().decode(authorization), StandardCharsets.UTF_8);

        String login = decryptedAuthorizationPart.split(":")[0];
        String password = decryptedAuthorizationPart.split(":")[1];

        String token;
        try {
            token = tokenService.getToken(login, password);
        } catch (PasswordMismatchException e) {
            return ResponseErrorHelper.responseError(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
