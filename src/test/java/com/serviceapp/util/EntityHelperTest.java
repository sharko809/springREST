package com.serviceapp.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import resources.TestConfiguration;

import static org.junit.Assert.assertNull;

/**
 * Tests for entity converters
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class EntityHelperTest {

    @Test
    public void dtoToUser() throws Exception {
        assertNull(EntityHelper.dtoToUser(null));
    }

    @Test
    public void dtoToReview() throws Exception {
        assertNull(EntityHelper.dtoToReview(null));
    }

    @Test
    public void userToDtoShort() throws Exception {
        assertNull(EntityHelper.userToDtoShort(null));
    }

}