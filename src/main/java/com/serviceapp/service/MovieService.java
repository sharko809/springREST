package com.serviceapp.service;

import com.serviceapp.entity.Movie;
import com.serviceapp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that accesses repository layer for movie data
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Get movie with provided ID from database
     *
     * @param id id of movie to find
     * @return <code>Movie</code> object if found, otherwise returns <code>null</code>
     */
    public Movie getMovie(Long id) {
        if (id != null) {
            return movieRepository.findOne(id);
        }
        return null;
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
     * @param id id of <code>Movie</code> to check. Must not be <code>null</code>.
     * @return <code>true</code> if an entity with the given id exists, <code>false</code> otherwise
     * @throws IllegalArgumentException if <code>id</code> is <code>null</code>
     */
    public Boolean ifMovieExists(Long id) {
        return movieRepository.exists(id);
    }

    public List<Movie> findMovieByTitle(String title) {
        return movieRepository.findByMovieNameContainsAllIgnoreCase(title);
    }

    /**
     * Get 10 movies with highest ratings
     *
     * @return List of 10 <code>Movie</code> objects with highest ratings
     */
    public List<Movie> findTopRated() {
        return movieRepository.findFirst10ByOrderByRatingDesc();
    }

    public Page<Movie> findAllPaged(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

}
