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
 * Tests for <code>ReviewService</code> class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class ReviewServiceTest {

    private static final Long NULL_LONG = null;
    private static final Long NEGATIVE_ID = -1L;
    private static final Long OK_ID = 1L;
    private static final Long ZERO_ID = 0L;
    @Autowired
    private ReviewService reviewService;

    @Test
    public void createReviewNull() throws Exception {
        assertNull(reviewService.createReview(null));
    }

    @Test
    public void getReview() throws Exception {
        // here id is 2L because there's no 1L
        assertNotNull(reviewService.getReview(2L));
        assertNull(reviewService.getReview(NEGATIVE_ID));
        assertNull(reviewService.getReview(ZERO_ID));
        assertNull(reviewService.getReview(NULL_LONG));
    }

    @Test
    public void updateReview() throws Exception {
        assertNull(reviewService.updateReview(null));
    }

    @Test
    public void deleteReview() throws Exception {
        reviewService.deleteReview(null);
    }

    @Test
    public void getReviewsByMovieId() throws Exception {
        assertNotNull(reviewService.getReviewsByMovieId(OK_ID));
        assertTrue(reviewService.getReviewsByMovieId(OK_ID).size() >= 1);
        assertTrue(reviewService.getReviewsByMovieId(null).isEmpty());
    }

    @Test
    public void ifReviewExists() throws Exception {
        assertTrue(reviewService.reviewExists(2L));
        assertFalse(reviewService.reviewExists(NULL_LONG));
        assertFalse(reviewService.reviewExists(ZERO_ID));
        assertFalse(reviewService.reviewExists(NEGATIVE_ID));
    }

}