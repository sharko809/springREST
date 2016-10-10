package com.serviceapp.repository;

import com.serviceapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface for accessing user data in database
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Save <code>User</code> entity and flush it immediately
     *
     * @param user <code>User</code> entity to save. Must not be <code>null</code>
     * @return saved <code>User</code> object. Use it to perform further manipulations with particular
     * <code>User</code> object
     * @throws IllegalArgumentException thrown if <code>user</code> is <code>null</code>
     */
    @Override
    <S extends User> S saveAndFlush(S user);

    /**
     * Looks up for user with provided ID in database
     *
     * @param id id of user to find. Must not be <code>null</code>
     * @return <code>User</code> object if found, otherwise returns <code>null</code>
     * @throws IllegalArgumentException thrown if <code>id</code> is <code>null</code>
     */
    @Override
    @Transactional(readOnly = true)
    User findOne(Long id);

    /**
     * Looks up for user with provided login in database
     *
     * @param login login of user to find. <code>null</code> will result in <code>null</code> return value
     * @return <code>User</code> object if found, otherwise returns <code>null</code>
     */
    @Transactional(readOnly = true)
    User findUserByLogin(String login);

    /**
     * Deletes given <code>User</code>
     *
     * @param user <code>User</code> entity to delete
     * @throws IllegalArgumentException thrown if <code>user</code> param is <code>null</code>
     */
    @Override
    void delete(User user);

    /**
     * Checks whether an entity of type <code>User</code> with the given id exists.
     *
     * @param id id of <code>User</code> to check. Must not be <code>null</code>.
     * @return <code>true</code> if an entity with the given id exists, <code>false</code> otherwise
     * @throws IllegalArgumentException thrown if <code>id</code> is <code>null</code>
     */
    @Override
    boolean exists(Long id);

    /**
     * Returns all instances of the <code>User</code> type.
     *
     * @return all <code>User</code> instances
     */
    @Override
    @Transactional(readOnly = true)
    List<User> findAll();

    /**
     * Get all <code>User</code> entities from database limited by <code>pageable</code> property
     *
     * @param pageable object implementing <code>Pageable</code> interface. Serves for pagination and sorting. If
     *                 <code>null</code>, method will return all entities.
     * @return iterable <code>Page</code> with <code>User</code> objects limited by params specified by
     * <code>pageable</code>
     */
    @Override
    @Transactional(readOnly = true)
    Page<User> findAll(Pageable pageable);

}
