package com.serviceapp.service;

import com.serviceapp.entity.User;
import com.serviceapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that accesses repository layer for user data
 *
 * @see User
 */
@Service
public class UserService {

    /**
     * Blank symbol (not a space). alt+255
     */
    private static final String BLANK_SYMBOL = " ";
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Save <code>User</code> entity
     *
     * @param user <code>User</code> entity to save. Must not be <code>null</code>, otherwise method returns
     *              <code>null</code>
     * @return saved <code>User</code> object. Use it to perform further manipulations with particular
     * <code>User</code> object. Returns <code>null</code> if trying to save <code>null</code>
     */
    public User createUser(User user) {
        return user == null ? null : userRepository.saveAndFlush(user);
    }

    /**
     * Update <code>User</code> entity
     *
     * @param user <code>User</code> entity to update. Must not be <code>null</code>, otherwise method returns
     *              <code>null</code>
     * @return updated <code>User</code> object. Use it to perform further manipulations with particular
     * <code>User</code> object. Returns <code>null</code> if trying to update <code>null</code>
     */
    public User updateUser(User user) {
        return user == null ? null : userRepository.saveAndFlush(user);
    }

    /**
     * Deletes given <code>User</code>
     *
     * @param user <code>User</code> entity to delete. Must not be <code>null</code>, otherwise nothing will happen
     */
    public void deleteUser(User user) {
        if (user != null) {
            userRepository.delete(user);
        }
    }

    /**
     * Get user with provided ID from database
     *
     * @param id id of user to find. Must not be <code>null</code>, otherwise returns <code>null</code>
     * @return <code>User</code> object if found, otherwise returns <code>null</code>
     */
    public User getUser(Long id) {
        return id == null ? null : userRepository.findOne(id);
    }

    /**
     * Get all instances of the <code>User</code> type.
     *
     * @return all <code>User</code> instances
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get all <code>User</code> entities from database limited by <code>pageable</code> property
     *
     * @param pageable object implementing <code>Pageable</code> interface. Serves for pagination and sorting. If
     *                 <code>null</code> - default scenario will be used (first 10 results).
     * @return iterable <code>Page</code> with <code>User</code> objects limited by params specified by
     * <code>pageable</code>
     */
    public Page<User> getAllUsersPaged(Pageable pageable) {
        return userRepository.findAll(pageable == null ? new PageRequest(0, 10) : pageable);
    }

    /**
     * Returns whether an entity of type <code>User</code> with the given id exists.
     *
     * @param id id of <code>User</code> to check. Must not be <code>null</code>, otherwise returns <code>false</code>
     * @return <code>true</code> if an entity with the given id exists, <code>false</code> otherwise
     */
    public Boolean userExists(Long id) {
        return id != null && userRepository.exists(id);
    }

    /**
     * Get user with provided login in database
     *
     * @param login login of user to find. Must not be <code>null</code>
     * @return <code>User</code> object if found, otherwise returns <code>null</code>
     */
    public User getUserByLogin(String login) {
        if (login != null) {
            if (login.isEmpty() || login.trim().isEmpty() || BLANK_SYMBOL.equals(login)) {
                return null;
            }
        } else {
            return null;
        }
        return userRepository.findUserByLogin(login);
    }

}
