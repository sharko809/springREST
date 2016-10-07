package com.serviceapp.repository;

import com.serviceapp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import resources.TestConfiguration;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * User repository test class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class UserRepositoryTest {

    private static final Long NULL_LONG = null;
    private static final Long NEGATIVE_ID = -1L;
    private static final Long OK_ID = 1L;
    private static final Long ZERO_ID = 0L;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveAndFlush() throws Exception {
        User user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setPassword("123");
        user.setAdmin(false);
        user.setBanned(false);
        assertNull(user.getId());
        User saved = userRepository.saveAndFlush(user);
        assertNotNull(saved.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createUserExceptionThrow() {
        userRepository.saveAndFlush(null);
    }

    @Test
    public void updateExistingUser() {
        User existing = userRepository.findOne(22L);
        Long oldId = existing.getId();
        String oldName = existing.getName();

        //update name
        existing.setName(existing.getName() + new Random().nextInt());

        User updated = userRepository.saveAndFlush(existing);
        String newName = updated.getName();
        Long newId = updated.getId();
        assertEquals(oldId, newId);
        assertNotEquals(oldName, newName);

    }

    @Test
    public void findOne() throws Exception {
        assertNotNull(userRepository.findOne(OK_ID));
        assertNull(userRepository.findOne(ZERO_ID));
        assertNull(userRepository.findOne(NEGATIVE_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOneNull() {
        userRepository.findOne(NULL_LONG);
    }

    @Test
    public void findUserByLoginNull() throws Exception {
        assertNull(userRepository.findUserByLogin(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete() throws Exception {
        userRepository.delete((User) null);
    }

    @Test
    public void findAll() throws Exception {
        assertNotNull(userRepository.findAll());
    }

    @Test
    public void findAllPageable() throws Exception {
        Page<User> usersPaged = userRepository.findAll((Pageable) null);
        assertNotNull(usersPaged);
        List<User> users = userRepository.findAll();
        assertEquals(users.size(), usersPaged.getTotalElements());
    }

}