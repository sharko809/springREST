package com.serviceapp.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import resources.TestConfiguration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for password encoder
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class PasswordManagerTest {

    @Autowired
    private PasswordManager passwordManager;

    @Test(expected = IllegalArgumentException.class)
    public void encodeNull() throws Exception {
        passwordManager.encode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeEmpty() {
        passwordManager.encode("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeSpace() {
        passwordManager.encode(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeEmptySpaces() {
        passwordManager.encode("  ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeEmptySymbol() {
        // alt+255
        passwordManager.encode(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeEmptySymbolSpace() {
        passwordManager.encode("  ");
    }

    @Test
    public void matches() throws Exception {
        String password = "123";
        String encoded = passwordManager.encode(password);
        assertTrue(passwordManager.matches(password, encoded));
    }

    @Test(expected = IllegalArgumentException.class)
    public void matchesNull() throws Exception{
        String password = null;
        String encoded = passwordManager.encode(password);
        passwordManager.matches(password, encoded);
    }

    @Test
    public void matchesEncodedNull() throws Exception{
        String password = "123";
        assertFalse(passwordManager.matches(password, null));
    }

}