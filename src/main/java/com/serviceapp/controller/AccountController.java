package com.serviceapp.controller;

import com.serviceapp.entity.ErrorEntity;
import com.serviceapp.entity.User;
import com.serviceapp.entity.dto.UserShortDto;
import com.serviceapp.entity.dto.UserTransferObject;
import com.serviceapp.security.PasswordManager;
import com.serviceapp.security.UserDetailsImpl;
import com.serviceapp.service.UserService;
import com.serviceapp.util.EntityHelper;
import com.serviceapp.util.PrincipalUtil;
import com.serviceapp.validation.marker.AccountValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Access user account info
     *
     * @param userId id of user which account is accessed
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if user account data acquired successfully</li>
     * <li>400 - if user id is invalid</li>
     * <li>403 - if user attempts to access another users account</li>
     * <li>500 - if some internal error that can't be handled at once occurred (primarily some severe db errors)</li>
     */
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
        UserShortDto userTransferObject = EntityHelper.userToDtoShort(user);

        return new ResponseEntity<>(userTransferObject, HttpStatus.OK);
    }

    /**
     * Updates user account info. If user left password field empty it would mean the password remains the same
     *
     * @param user   <code>UserTransferObject</code> populated with user data to update
     * @param errors errors generated if user data validation failed
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if user account data updated successfully</li>
     * <li>400 - if there were errors in user data</li>
     * <li>403 - if trying to change login to already existing one</li>
     * <li>500 - if some internal error that can't be handled at once occurred (primarily some severe db errors)</li>
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateAccount(@Validated({Default.class, AccountValidation.class})
                                        @RequestBody UserTransferObject user, BindingResult errors) {
        if (errors.hasErrors()) {
            List<String> validationErrors = errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            ErrorEntity error = new ErrorEntity(HttpStatus.BAD_REQUEST, validationErrors);
            return new ResponseEntity<>(error, error.getStatus());
        }
        Long currentUserId = PrincipalUtil.getCurrentPrincipal().getId();
        User userCheck = userService.getUserByLogin(user.getLogin());
        if (userCheck != null) {
            if (userCheck.getId().equals(currentUserId)) {
                ErrorEntity error = new ErrorEntity(HttpStatus.FORBIDDEN, "This login is already in use");
                return new ResponseEntity<>(error, error.getStatus());
            }
        }

        User currentUser = userService.getUser(currentUserId);
        currentUser.setName(user.getName());
        currentUser.setLogin(user.getLogin());
        if (!user.getPassword().isEmpty()) {
            currentUser.setPassword(passwordManager.encode(user.getPassword()));
        }

        User updatedUser = userService.updateUser(currentUser);
        if (updatedUser == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "User is not updated");
            return new ResponseEntity<>(error, error.getStatus());
        }

        resetAuthentication(currentUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Replaces current Authentication in SecurityContext with the new one - updates user details.
     *
     * @param currentUser user to be populated into Authentication
     */
    private void resetAuthentication(User currentUser) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(
                new UserDetailsImpl(currentUser.getId(), currentUser.getName(), currentUser.getLogin(),
                        currentUser.getPassword(), authentication.getAuthorities(), currentUser.isBanned()),
                currentUser.getPassword(), authentication.getAuthorities()));
    }

}
