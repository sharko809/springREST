package com.serviceapp.controller;

import com.serviceapp.entity.ErrorEntity;
import com.serviceapp.entity.dto.UserTransferObject;
import com.serviceapp.security.PasswordManager;
import com.serviceapp.service.UserService;
import com.serviceapp.util.EntityHelper;
import com.serviceapp.validation.marker.RegistrationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller handling user registration
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

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
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity register(@Validated({Default.class, RegistrationValidation.class})
                                   @RequestBody(required = false) UserTransferObject user, BindingResult errors) {
        if (user == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.UNPROCESSABLE_ENTITY, "No user data detected");
            return new ResponseEntity<>(error, error.getStatus());
        }

        if (errors.hasErrors()) {
            List<String> validationErrors = errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            ErrorEntity error = new ErrorEntity(HttpStatus.BAD_REQUEST, validationErrors);
            return new ResponseEntity<>(error, error.getStatus());
        }

        Boolean userNotExists = userService.getUserByLogin(user.getLogin()) == null;
        if (userNotExists) {
            String encodedPassword = passwordManager.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setAdmin(false);
            user.setBanned(false);
            userService.createUser(EntityHelper.dtoToUser(user));
        } else {
            ErrorEntity error = new ErrorEntity(HttpStatus.CONFLICT, "User with such login already exists");
            return new ResponseEntity<>(error, error.getStatus());
        }

        String success = "User <b>" + user.getLogin() + "</b> created successfully";
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

}
