package com.serviceapp.service;

import com.serviceapp.entity.Movie;
import com.serviceapp.repository.MovieRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that accesses repository layer for movie data
 *
 * @see Movie
 */
@Service
public class MovieService {

    private static final Logger LOGGER = LogManager.getLogger();
    /**
     * Blank symbol (not a space). alt+255
     */
    private static final String BLANK_SYMBOL = " ";
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Save <code>Movie</code> entity
     *
     * @param movie <code>Movie</code> entity to save. Must not be <code>null</code>, otherwise method returns
     *              <code>null</code>
     * @return saved <code>Movie</code> object. Use it to perform further manipulations with particular
     * <code>Movie</code> object. Returns <code>null</code> if trying to save <code>null</code>
     */
    public Movie createMovie(Movie movie) {
        return movie == null ? null : movieRepository.saveAndFlush(movie);
    }

    /**
     * Update <code>Movie</code> entity
     *
     * @param movie <code>Movie</code> entity to update. Must not be <code>null</code>, otherwise method returns
     *              <code>null</code>
     * @return updated <code>Movie</code> object. Use it to perform further manipulations with particular
     * <code>Movie</code> object. Returns <code>null</code> if trying to update <code>null</code>
     */
    public Movie updateMovie(Movie movie) {
        return movie == null ? null : movieRepository.saveAndFlush(movie);
    }

    /**
     * Deletes given <code>Movie</code>
     *
     * @param movie <code>Movie</code> entity to delete. Must not be <code>null</code>, otherwise nothing will happen
     */
    public void deleteMovie(Movie movie) {
        if (movie != null) {
            movieRepository.delete(movie);
        }
    }

    /**
     * Get movie with provided ID from database
     *
     * @param id id of movie to find. Must not be <code>null</code>, otherwise returns <code>null</code>
     * @return <code>Movie</code> object if found, otherwise returns <code>null</code>
     */
    public Movie getMovie(Long id) {
        return id == null ? null : movieRepository.findOne(id);
    }

    /**
     * Get all instances of the <code>Movie</code> type.
     *
     * @return all <code>Movie</code> instances
     */
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * Count the number of <code>Movie</code> entities available.
     *
     * @return the number of <code>Movie</code> entities
     */
    public Long countMovies() {
        return movieRepository.count();
    }

    /**
     * Returns whether an entity of type <code>Movie</code> with the given id exists.
     *
     * @param id id of <code>Movie</code> to check. Must not be <code>null</code>, otherwise returns <code>false</code>
     * @return <code>true</code> if an entity with the given id exists, <code>false</code> otherwise
     */
    public Boolean movieExists(Long id) {
        return id != null && movieRepository.exists(id);
    }

    /**
     * Get movies with given title (or its part) in database. Spaces, empty symbols and empty string will result in
     * <code>null</code> output.
     *
     * @param title    movie title (or its part) to look for. Must not be <code>null</code> (or will return
     *                 <code>null</code>)
     * @param pageable object implementing <code>Pageable</code> interface. Serves for pagination and sorting
     * @return iterable <code>Page</code> with <code>Movie</code> objects matching search param if any found and limited
     * by params specified by <code>pageable</code>, otherwise returns <code>null</code>
     */
    public Page<Movie> findMovieByTitle(String title, Pageable pageable) {
        if (title != null) {
            if (title.isEmpty() || BLANK_SYMBOL.equals(title) || title.trim().isEmpty()) {
                return null;
            }
        } else {
            return null;
        }
        return movieRepository.findByMovieNameContainsAllIgnoreCase(title, pageable);
    }

    /**
     * Get 10 movies with highest ratings
     *
     * @return List of 10 <code>Movie</code> objects with highest ratings
     */
    public List<Movie> findTopRated() {
        return movieRepository.findFirst10ByOrderByRatingDesc();
    }

    /**
     * Get all <code>Movie</code> entities from database limited by <code>pageable</code> property
     *
     * @param pageable object implementing <code>Pageable</code> interface. Serves for pagination and sorting. If
     *                 <code>null</code> - default scenario will be used (first 5 results).
     * @return iterable <code>Page</code> with <code>Movie</code> objects limited by params specified by
     * <code>pageable</code>
     */
    public Page<Movie> findAllPaged(Pageable pageable) {
        return movieRepository.findAll(pageable == null ? new PageRequest(0, 5) : pageable);
    }

}
