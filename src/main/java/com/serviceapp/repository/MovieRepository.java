package com.serviceapp.repository;

import com.serviceapp.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface for accessing movie data in database
 */
@Repository
@Transactional
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Looks up for movie with provided ID in database
     *
     * @param id id of movie to find
     * @return <code>Movie</code> object if found, otherwise returns <code>null</code>
     */
    @Transactional(readOnly = true)
    Movie findOne(Long id);

    /**
     * Returns all instances of the <code>Movie</code> type.
     *
     * @return all <code>Movie</code> instances
     */
    @Transactional(readOnly = true)
    List<Movie> findAll();

    /**
     * Returns the number of <code>Movie</code> entities available.
     *
     * @return the number of <code>Movie</code> entities
     */
    @Transactional(readOnly = true)
    long count();

    /**
     * Checks whether an entity of type <code>Movie</code> with the given id exists.
     *
     * @param id id of <code>Movie</code> to check. Must not be <code>null</code>.
     * @return <code>true</code> if an entity with the given id exists, <code>false</code> otherwise
     * @throws IllegalArgumentException if <code>id</code> is <code>null</code>
     */
    @Transactional(readOnly = true)
    boolean exists(Long id);

    @Transactional(readOnly = true)
    List<Movie> findByMovieNameContainsAllIgnoreCase(String moviename);

    @Transactional(readOnly = true)
    List<Movie> findFirst10ByOrderByRatingDesc();

    Page<Movie> findAll(Pageable pageable);

}
