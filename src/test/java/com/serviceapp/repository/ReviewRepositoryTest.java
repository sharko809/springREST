package com.serviceapp.repository;

import com.serviceapp.entity.Review;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import resources.TestConfiguration;

import java.sql.Date;

import static org.junit.Assert.*;

/**
 * Review repository test class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class ReviewRepositoryTest {

    private static final Long NULL_LONG = null;
    private static final Long NEGATIVE_ID = -1L;
    private static final Long OK_ID = 1L;
    private static final Long ZERO_ID = 0L;
    @Autowired
    private ReviewRepository reviewRepository;

    @Test(expected = IllegalArgumentException.class)
    public void saveAndFlushNull() throws Exception {
        reviewRepository.saveAndFlush(null);
    }

    @Test
    public void saveAndFlush() {
        Review review = new Review();
        review.setMovieId(OK_ID);
        review.setUserId(OK_ID);
        review.setTitle("title");
        review.setPostDate(new Date(new java.util.Date().getTime()));
        review.setReviewText("texttext");
        review.setRating(10);
        review.setPostDate(new Date(new java.util.Date().getTime()));
        assertNull(review.getId());
        Review saved = reviewRepository.saveAndFlush(review);
        assertNotNull(saved.getId());
    }

    @Test
    public void findOne() throws Exception {
        // here id is 2L because there's no 1L
        assertNotNull(reviewRepository.findOne(2L));
        assertNull(reviewRepository.findOne(ZERO_ID));
        assertNull(reviewRepository.findOne(NEGATIVE_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOneExceptionThrow() {
        reviewRepository.findOne(NULL_LONG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete() throws Exception {
        reviewRepository.delete((Review) null);
    }

    @Test
    public void findReviewsByMovieId() throws Exception {
        assertNotNull(reviewRepository.findReviewsByMovieId(OK_ID));
        assertTrue(reviewRepository.findReviewsByMovieId(OK_ID).size() >= 1);
    }

    @Test
    public void exists() {
        assertTrue(reviewRepository.exists(2L));
        assertFalse(reviewRepository.exists(ZERO_ID));
        assertFalse(reviewRepository.exists(NEGATIVE_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void existsExceptionThrow()  {
        reviewRepository.exists(NULL_LONG);
    }

}