package com.serviceapp.controller;

import com.serviceapp.entity.Movie;
import com.serviceapp.service.MovieService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dsharko on 10/3/2016.
 */
@RestController
public class MovieController {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Integer RECORDS_PER_PAGE = 5;
    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Movie> movie(@PathVariable(name = "id") Long id) {
        Movie movie = movieService.getMovie(id);
        if (movie == null) {
            return null;
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Movie> allMovies() {
        return movieService.getAllMovies();
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long countMovies() {
        return movieService.countMovies();
    }

    @RequestMapping(value = "/ex", method = RequestMethod.GET)
    public ResponseEntity<String> ifMovieExists(@RequestParam(name = "id", defaultValue = "1") Long id) {
        if (id < 1) {
            return new ResponseEntity<>("Illegal id value", HttpStatus.BAD_REQUEST);
        }
        Boolean exists = movieService.ifMovieExists(id);
        return new ResponseEntity<>("Movie " + id + " exists: " + exists, HttpStatus.OK);
    }

    @RequestMapping(value = "/mov", method = RequestMethod.GET)
    public Page<Movie> paged(Pageable pageable) {
        return movieService.findAllPaged(pageable);
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public List<Movie> topRated() {
        return movieService.findTopRated();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<Movie> search(@RequestParam(name = "t", defaultValue = " ") String title, Pageable pageable) {
        int pageNumber = pageable.getPageNumber() < 0 ? 0 : pageable.getPageNumber();
        return movieService.findMovieByTitle(title, new PageRequest(pageNumber, RECORDS_PER_PAGE, null));
    }

}
