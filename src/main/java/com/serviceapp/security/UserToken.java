package com.serviceapp.security;

/**
 * Created by dsharko on 10/17/2016.
 */
public class UserToken {

    private String login;
    private String password;
    private Boolean banned;
    private Boolean admin;

    public UserToken() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", banned=" + banned +
                ", admin=" + admin +
                '}';
    }
}
