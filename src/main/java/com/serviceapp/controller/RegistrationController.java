package com.serviceapp.controller;

import com.serviceapp.entity.User;
import com.serviceapp.entity.dto.UserTransferObject;
import com.serviceapp.security.PasswordManager;
import com.serviceapp.service.UserService;
import com.serviceapp.util.EntityHelper;
import com.serviceapp.util.ResponseErrorHelper;
import com.serviceapp.validation.marker.RegistrationValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller handling user registration
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private static final Logger LOGGER = LogManager.getLogger();
    private UserService userService;
    private PasswordManager passwordManager;

    @Autowired
    public RegistrationController(UserService userService, PasswordManager passwordManager) {
        this.userService = userService;
        this.passwordManager = passwordManager;
    }

    /**
     * Processes user registration.
     *
     * @param user   object populated with data from user (username, login, password)
     * @param errors errors generated if <code>user</code> param failed validation
     * @return message(s) indicating the status of registration. It could be either error messages describing cause of
     * error so the user can fix it, or the success message if registration completed.
     */
    @PostMapping
    public ResponseEntity register(@Validated({Default.class, RegistrationValidation.class})
                                   @RequestBody(required = false) UserTransferObject user, BindingResult errors) {
        if (user == null) {
            LOGGER.warn("No user data detected in request");
            return ResponseErrorHelper.responseError(HttpStatus.UNPROCESSABLE_ENTITY, "No user data detected");
        }

        if (errors.hasErrors()) {
            List<String> validationErrors = errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseErrorHelper.responseError(HttpStatus.BAD_REQUEST, validationErrors);
        }

        Boolean userNotExists = userService.getUserByLogin(user.getLogin()) == null;
        if (userNotExists) {
            String encodedPassword = passwordManager.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setAdmin(false);
            user.setBanned(false);
            User created = userService.createUser(EntityHelper.dtoToUser(user));
            if (created == null) {
                LOGGER.error("User creation failed. Please, see all logs for details");
                return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "User not created");
            }
        } else {
            return ResponseErrorHelper.responseError(HttpStatus.CONFLICT, "User with such login already exists");
        }

        LOGGER.debug("User {} created", user.getLogin());
        String success = "User " + user.getLogin() + " created successfully";
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

}
