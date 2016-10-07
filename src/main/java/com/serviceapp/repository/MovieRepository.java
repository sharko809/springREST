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
     * Save <code>Movie</code> entity and flush it immediately
     *
     * @param movie <code>Movie</code> entity to save. Must not be <code>null</code>
     * @return saved <code>Movie</code> object. Use it to perform further manipulations with particular
     * <code>Movie</code> object
     * @throws IllegalArgumentException thrown if <code>movie</code> is <code>null</code>
     */
    @Override
    <S extends Movie> S saveAndFlush(S movie);

    /**
     * Deletes given <code>Movie</code>
     *
     * @param movie <code>Movie</code> entity to delete
     * @throws IllegalArgumentException thrown if <code>movie</code> param is <code>null</code>
     */
    @Override
    void delete(Movie movie);

    /**
     * Looks up for movie with provided ID in database
     *
     * @param id id of movie to find. Must not be <code>null</code>
     * @return <code>Movie</code> object if found, otherwise returns <code>null</code>
     * @throws IllegalArgumentException thrown if <code>id</code> is <code>null</code>
     */
    @Override
    @Transactional(readOnly = true)
    Movie findOne(Long id);

    /**
     * Returns all instances of the <code>Movie</code> type.
     *
     * @return all <code>Movie</code> instances
     */
    @Override
    @Transactional(readOnly = true)
    List<Movie> findAll();

    /**
     * Returns the number of <code>Movie</code> entities available.
     *
     * @return the number of <code>Movie</code> entities
     */
    @Override
    @Transactional(readOnly = true)
    long count();

    /**
     * Checks whether an entity of type <code>Movie</code> with the given id exists.
     *
     * @param id id of <code>Movie</code> to check. Must not be <code>null</code>.
     * @return <code>true</code> if an entity with the given id exists, <code>false</code> otherwise
     * @throws IllegalArgumentException thrown if <code>id</code> is <code>null</code>
     */
    @Override
    @Transactional(readOnly = true)
    boolean exists(Long id);

    /**
     * Searches for movies with given title (or its part) in database
     *
     * @param title    movie title (or its part) to look for. Must not be null
     * @param pageable object implementing <code>Pageable</code> interface. Serves for pagination and sorting
     * @return iterable <code>Page</code> with <code>Movie</code> objects matching search param if any found and limited
     * by params specified by <code>pageable</code>, otherwise returns <code>null</code>
     * @throws IllegalArgumentException thrown if <code>title</code> is <code>null</code>
     */
    @Transactional(readOnly = true)
    Page<Movie> findByMovieNameContainsAllIgnoreCase(String title, Pageable pageable);

    /**
     * Searches for movies with highest ratings and gets first 10 of them (if there are 10+ records in database).
     *
     * @return <code>List</code> with 10 <code>Movies</code> with highest rating
     */
    @Transactional(readOnly = true)
    List<Movie> findFirst10ByOrderByRatingDesc();

    /**
     * Get all <code>Movie</code> entities from database limited by <code>pageable</code> property
     *
     * @param pageable object implementing <code>Pageable</code> interface. Serves for pagination and sorting. If
     *                 <code>null</code>, method will return all entities.
     * @return iterable <code>Page</code> with <code>Movie</code> objects limited by params specified by
     * <code>pageable</code>
     */
    @Override
    @Transactional(readOnly = true)
    Page<Movie> findAll(Pageable pageable);

}
