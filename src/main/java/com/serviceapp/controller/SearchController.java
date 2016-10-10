package com.serviceapp.controller;

import com.serviceapp.entity.Movie;
import com.serviceapp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for performing movies search. Available to all users
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    private static final Integer RECORDS_PER_PAGE = 5;
    private MovieService movieService;

    @Autowired
    public SearchController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Method returns pageable movies list found by movie title specified by <code>title</code> parameter set by user
     *
     * @param title    title of movie to look for
     * @param pageable <code>org.springframework.data.domain.Pageable</code> for convenient pagination and sorting
     * @return <code>Page</code> of <code>Movies</code> objects if any found with specified title
     */
    @RequestMapping(method = RequestMethod.GET)
    public Page<Movie> search(@RequestParam(name = "t", defaultValue = " ") String title, Pageable pageable) {
        int pageNumber = pageable.getPageNumber() < 0 ? 0 : pageable.getPageNumber();
        return movieService.findMovieByTitle(title, new PageRequest(pageNumber, RECORDS_PER_PAGE, null));
    }

}
