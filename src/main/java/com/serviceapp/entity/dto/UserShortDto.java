package com.serviceapp.entity.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Helper class used as DTO(shortened) for users.
 */
public class UserShortDto {

    boolean banned;
    private Long id;
    @NotNull
    @Size(min = 3, max = 20, message = "{username.size}")
    @Pattern(regexp = "[\\p{L}0-9]+([ '-][\\p{L}0-9]+)*", message = "{username.pattern}")
    private String name;
    @NotNull
    @Size(min = 3, max = 60, message = "{login.size}")
    @Pattern(regexp = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "{login.pattern}")
    private String login;

    public UserShortDto() {
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
