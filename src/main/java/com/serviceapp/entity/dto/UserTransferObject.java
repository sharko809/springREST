package com.serviceapp.entity.dto;

import com.serviceapp.validation.annotation.ValidUserTransferObjectPassword;
import com.serviceapp.validation.marker.AccountValidation;
import com.serviceapp.validation.marker.CreateUserValidation;
import com.serviceapp.validation.marker.RegistrationValidation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Helper class used as DTO for users.
 */
public class UserTransferObject {

    @NotNull
    @Size(min = 3, max = 20, message = "{username.size}")
    @Pattern(regexp = "[\\p{L}0-9]+([ '-][\\p{L}0-9]+)*", message = "{username.pattern}")
    private String name;

    @NotNull
    @Size(min = 3, max = 60, message = "{login.size}")
    @Pattern(regexp = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "{login.pattern}")
    private String login;

    @NotNull(groups = {RegistrationValidation.class, CreateUserValidation.class})
    @Size(min = 3, max = 15, message = "{password.size}",
            groups = {RegistrationValidation.class, CreateUserValidation.class})
    @ValidUserTransferObjectPassword(min = 3, max = 15, message = "{password.size}", groups = AccountValidation.class)
    private String password;

    private Boolean admin;

    private Boolean banned;

    public UserTransferObject() {
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}