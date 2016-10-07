package com.serviceapp.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import resources.TestConfiguration;

import static org.junit.Assert.*;

/**
 * Tests for <code>UserService</code> class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class UserServiceTest {

    private static final Long NULL_LONG = null;
    private static final Long NEGATIVE_ID = -1L;
    private static final Long OK_ID = 1L;
    private static final Long ZERO_ID = 0L;
    @Autowired
    private UserService userService;

    @Test
    public void createUser() throws Exception {
        assertNull(userService.createUser(null));
    }

    @Test
    public void updateUser() throws Exception {
        assertNull(userService.updateUser(null));
    }

    @Test
    public void deleteUser() throws Exception {
        userService.deleteUser(null);
    }

    @Test
    public void getUser() throws Exception {
        assertNotNull(userService.getUser(OK_ID));
        assertNull(userService.getUser(NULL_LONG));
        assertNull(userService.getUser(NEGATIVE_ID));
        assertNull(userService.getUser(ZERO_ID));
    }

    @Test
    public void getAllUsers() throws Exception {
        assertNotNull(userService.getAllUsers());
        assertTrue(userService.getAllUsers().size() > 5);
    }

    @Test
    public void getAllUsersPaged() throws Exception {
        assertTrue(userService.getAllUsersPaged(null).getNumberOfElements() == 10);
    }

    @Test
    public void getUserByLogin() throws Exception {
        assertNull(userService.getUserByLogin(""));
        // blank symbol (alt+255)
        assertNull(userService.getUserByLogin(" "));
        assertNull(userService.getUserByLogin(null));
        // space
        assertNull(userService.getUserByLogin(" "));
    }

}