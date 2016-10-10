package com.serviceapp.controller;

import com.serviceapp.entity.ErrorEntity;
import com.serviceapp.entity.User;
import com.serviceapp.entity.dto.UserShortDto;
import com.serviceapp.security.PasswordManager;
import com.serviceapp.service.UserService;
import com.serviceapp.util.EntityConverter;
import com.serviceapp.util.PrincipalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling user account activity
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private UserService userService;
    private PasswordManager passwordManager;

    @Autowired
    public AccountController(UserService userService, PasswordManager passwordManager) {
        this.userService = userService;
        this.passwordManager = passwordManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity account(@RequestParam(value = "id", defaultValue = "0") Long userId) {
        Long currentUserId = PrincipalUtil.getCurrentPrincipal().getId();
        if (userId < 1) {
            ErrorEntity error = new ErrorEntity(HttpStatus.BAD_REQUEST, "Disallowed id detected");
            return new ResponseEntity<>(error, error.getStatus());
        } else if (!currentUserId.equals(userId)) {
            ErrorEntity error = new ErrorEntity(HttpStatus.FORBIDDEN, "Forbidden request");
            return new ResponseEntity<>(error, error.getStatus());
        }

        User user = userService.getUser(userId);
        if (user == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get user");
            return new ResponseEntity<>(error, error.getStatus());
        }
        UserShortDto userTransferObject = EntityConverter.userToDtoShort(user);

        return new ResponseEntity<>(userTransferObject, HttpStatus.OK);
    }

}
