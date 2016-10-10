package com.serviceapp.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Class representing <code>User</code> entity.
 */
@Entity
public class User {

    /**
     * User id from database
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    /**
     * Username used to display in UI
     */
    @NotNull
    @Size(min = 3, max = 20, message = "{username.size}")
    @Pattern(regexp = "[a-zA-zа-яА-яё0-9]+([ '-][a-zA-Zа-яА-Яё0-9]+)*", message = "{username.pattern}")
    @Column(name = "username")
    private String name;

    /**
     * Login is used to log in the service.
     * Only visible to admin.
     */
    @NotNull
    @Size(min = 3, max = 60, message = "{login.size}")
    @Pattern(regexp = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "{login.pattern}")
    @Column(name = "login")
    private String login;

    /**
     * User password to access service.
     */
    @NotNull
    @Column(name = "password")
    private String password;

    /**
     * Field indicating if user has admin rights.
     * <b>true</b> - user has admin rights
     */
    @Column(name = "isadmin")
    private Boolean admin;

    /**
     * Field indicating if user is banned.
     * Banned users can't access service functions.
     * <b>true</b> - user is banned
     */
    @Column(name = "isbanned")
    private Boolean banned;

    public User() {
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Boolean isAdmin() {
        return admin;
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

    public Boolean getAdmin() {
        return this.admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                ", banned=" + banned +
                '}';
    }
}