package com.serviceapp.repository;

import com.serviceapp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface for accessing review data in database
 */
@Repository
@Transactional
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Save <code>Review</code> entity and flush it immediately
     *
     * @param review <code>Review</code> entity to save. Must not be <code>null</code>
     * @return saved <code>Review</code> object. Use it to perform further manipulations with particular
     * <code>Review</code> object
     * @throws IllegalArgumentException thrown if <code>review</code> is <code>null</code>
     */
    @Override
    <S extends Review> S saveAndFlush(S review);

    /**
     * Looks up for review with provided ID in database
     *
     * @param id id of review to find. Must not be <code>null</code>
     * @return <code>Review</code> object if found, otherwise returns <code>null</code>
     * @throws IllegalArgumentException thrown if <code>id</code> is <code>null</code>
     */
    @Override
    @Transactional(readOnly = true)
    Review findOne(Long id);

    /**
     * Deletes given <code>Review</code>
     *
     * @param review <code>Review</code> entity to delete
     * @throws IllegalArgumentException thrown if <code>review</code> param is <code>null</code>
     */
    @Override
    void delete(Review review);

    /**
     * Searches for reviews for movie with provided id.
     *
     * @param movieId ID of movie which this review refers to
     * @return List of <code>Review</code> objects if found any. Otherwise returns empty List.
     */
    @Transactional(readOnly = true)
    List<Review> findReviewsByMovieId(Long movieId);

    /**
     * Checks whether an entity of type <code>Review</code> with the given id exists.
     *
     * @param id id of <code>Review</code> to check. Must not be <code>null</code>.
     * @return <code>true</code> if an entity with the given id exists, <code>false</code> otherwise
     * @throws IllegalArgumentException thrown if <code>id</code> is <code>null</code>
     */
    @Override
    @Transactional(readOnly = true)
    boolean exists(Long id);
}
