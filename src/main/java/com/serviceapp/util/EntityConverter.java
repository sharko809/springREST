package com.serviceapp.util;

import com.serviceapp.entity.User;
import com.serviceapp.entity.dto.UserTransferObject;

/**
 * Helper class to convert DTO to entities and backwards.
 */
public class EntityConverter {

    /**
     * Converts provided <code>UserTransferObject</code> to <code>User</code> object
     *
     * @param userTransferObject <code>UserTransferObject</code> to convert to <code>User</code>
     * @return <code>User</code> object with fields populated from provided <code>UserTransferObject</code>
     * @see UserTransferObject
     * @see User
     */
    public static User dtoToUser(UserTransferObject userTransferObject) {
        User user = new User();
        user.setName(userTransferObject.getName());
        user.setLogin(userTransferObject.getLogin());
        user.setPassword(userTransferObject.getPassword());
        user.setAdmin(userTransferObject.getAdmin());
        user.setBanned(userTransferObject.getBanned());
        return user;
    }

}
