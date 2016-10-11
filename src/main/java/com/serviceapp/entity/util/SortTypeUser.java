package com.serviceapp.entity.util;

/**
 * Allowed parameters by which user sorting can be performed
 */
public enum SortTypeUser {

    /**
     * Sort by user ID (default)
     */
    ID(0, "id"),
    /**
     * Sort by login
     */
    LOGIN(1, "login"),
    /**
     * Sort by name
     */
    USERNAME(2, "username"),
    /**
     * Sort by admin authorities
     */
    ADMIN(3, "admin"),
    /**
     * Sort by banned status
     */
    BANNED(4, "banned");

    private final int id;
    private final String value;

    SortTypeUser(int id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * Check if provided value is a valid user sort type
     *
     * @param value value to check if it's valid sort type
     * @return <code>true</code> if <code>value</code> param is a valid user sort type, <code>false</code> otherwise
     */
    public static boolean isUserSortType(String value) {
        for (SortTypeUser e : values()) {
            if (e.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    /**
     * Return the parameter value by which sorting is performed
     */
    public String getValue() {
        return value;
    }

}
