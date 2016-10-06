package com.serviceapp.entity.dto;

/**
 * Helper class used as DTO for users.
 */
public class UserTransferObject {

    private String name;

    private String login;

    private String password;

    private Boolean admin;

    public UserTransferObject() {}

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