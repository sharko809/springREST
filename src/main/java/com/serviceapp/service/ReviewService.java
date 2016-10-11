package com.serviceapp.service;

import com.serviceapp.entity.Review;
import com.serviceapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that accesses repository layer for review data
 *
 * @see Review
 */
@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Save <code>Review</code> entity
     *
     * @param review <code>Review</code> entity to save. Must not be <code>null</code>, otherwise method
     *               returns <code>null</code>
     * @return saved <code>Review</code> object. Use it to perform further manipulations with particular
     * <code>Review</code> object. Returns <code>null</code> if trying to save <code>null</code>
     */
    public Review createReview(Review review) {
        return review == null ? null : reviewRepository.saveAndFlush(review);
    }

    /**
     * Get review with provided ID in database
     *
     * @param id id of review to find. Must not be <code>null</code>, otherwise method returns <code>null</code>
     * @return <code>Review</code> object if found, otherwise returns <code>null</code>
     */
    public Review getReview(Long id) {
        return id == null ? null : reviewRepository.findOne(id);
    }

    /**
     * Update <code>Review</code> entity
     *
     * @param review <code>Review</code> entity to update. Must not be <code>null</code>, otherwise method
     *               returns <code>null</code>
     * @return updated <code>Review</code> object. Use it to perform further manipulations with particular
     * <code>Review</code> object. Returns <code>null</code> if trying to update <code>null</code>
     */
    public Review updateReview(Review review) {
        return review == null ? null : reviewRepository.saveAndFlush(review);
    }

    /**
     * Deletes given <code>Review</code>
     *
     * @param review <code>Review</code> entity to delete. Must not be <code>null</code>, otherwise nothing will happen
     */
    public void deleteReview(Review review) {
        if (review != null) {
            reviewRepository.delete(review);
        }
    }

    /**
     * Get reviews for movie with provided id.
     *
     * @param movieId ID of movie which this review refers to
     * @return List of <code>Review</code> objects if found any. Otherwise returns empty List.
     */
    public List<Review> getReviewsByMovieId(Long movieId) {
        return movieId == null ? new ArrayList<>() : reviewRepository.findReviewsByMovieId(movieId);
    }

    /**
     * Returns whether an entity of type <code>Review</code> with the given id exists.
     *
     * @param id id of <code>Review</code> to check. Must not be <code>null</code>, otherwise returns <code>false</code>
     * @return <code>true</code> if an entity with the given id exists, <code>false</code> otherwise
     */
    public Boolean ifReviewExists(Long id) {
        return id != null && reviewRepository.exists(id);
    }

}
