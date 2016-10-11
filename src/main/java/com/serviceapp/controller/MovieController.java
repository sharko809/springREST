package com.serviceapp.controller;

import com.serviceapp.entity.ErrorEntity;
import com.serviceapp.entity.Movie;
import com.serviceapp.entity.Review;
import com.serviceapp.entity.dto.ReviewTransferObject;
import com.serviceapp.entity.util.MovieContainer;
import com.serviceapp.service.MovieService;
import com.serviceapp.service.ReviewService;
import com.serviceapp.service.UserService;
import com.serviceapp.util.EntityHelper;
import com.serviceapp.util.PrincipalUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for all activity concerning movies that is available to simple users.
 */
@RestController
@RequestMapping("/movies")
public class MovieController {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Integer RECORDS_PER_PAGE = 5;
    private MovieService movieService;
    private ReviewService reviewService;
    private UserService userService;

    @Autowired
    public MovieController(MovieService movieService, ReviewService reviewService, UserService userService) {
        this.movieService = movieService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Movie> paged(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() < 0 ? 0 : pageable.getPageNumber();// TODO I can add sorting. Seems ok
        return movieService.findAllPaged(new PageRequest(pageNumber, RECORDS_PER_PAGE, null));// TODO handle max page range
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity movie(@PathVariable(name = "id") Long id) {
        if (!movieService.ifMovieExists(id)) {
            ErrorEntity error = new ErrorEntity(HttpStatus.NOT_FOUND, "No such movie found");
            return new ResponseEntity<>(error, error.getStatus());
        }
        Movie movie = movieService.getMovie(id);
        if (movie == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get movie");
            return new ResponseEntity<>(error, error.getStatus());
        }
        MovieContainer container = EntityHelper.completeMovie(id, movieService, reviewService, userService);
        // TODO think about refactoring movie presentation
        return new ResponseEntity<>(container, HttpStatus.OK);
    }

    /**
     * Performs review posting and accompanied functions (recalculating movie rating)
     *
     * @param id      path variable - id of movie for which review is written
     * @param reviewObject object holding review data (title, text, rating)
     * @param errors       errors generated if <code>reviewObject</code> param failed validation
     * @return status code representing the status of the operation:
     * <li><code>204</code> - if no review content has been provided</li>
     * <li><code>400</code> - if review data failed validation</li>
     * <li><code>500</code> - if review has not been created (to some unexpected reasons)</li>
     * <li><code>200</code> - if review has been created successfully</li>
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity postReview(@PathVariable Long id,
                                     @Validated @RequestBody(required = false) ReviewTransferObject reviewObject,
                                     BindingResult errors) {
        if (!movieService.ifMovieExists(id)) {
            ErrorEntity error = new ErrorEntity(HttpStatus.NOT_FOUND, "No such movie found");
            return new ResponseEntity<>(error, error.getStatus());
        }
        if (errors.hasErrors()) {
            List<String> validationErrors = errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            ErrorEntity error = new ErrorEntity(HttpStatus.BAD_REQUEST, validationErrors);
            return new ResponseEntity<>(error, error.getStatus());
        }

        Long currentUserId = PrincipalUtil.getCurrentPrincipal().getId();
        Date postDate = new Date(new java.util.Date().getTime());

        Review review = EntityHelper.dtoToReview(reviewObject);
        review.setMovieId(id);
        review.setUserId(currentUserId);
        review.setPostDate(postDate);
        Review createdReview = reviewService.createReview(review);
        if (createdReview == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Review is not created");
            return new ResponseEntity<>(error, error.getStatus());
        }
        updateMovieRating(id, reviewObject.getRating());
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Get top 10 movies with highest rating
     *
     * @return <code>List</code> of 10 movies with highest rating
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public List<Movie> topRated() {
        return movieService.findTopRated();
    }

    /**
     * Updates movie current rating taking in account new rating put by user
     *
     * @param movieId id of movie to update rating
     * @param rating  new rating set by user
     */
    private void updateMovieRating(Long movieId, Integer rating) {
        Movie movieToUpdate = movieService.getMovie(movieId);
        List<Review> reviews = reviewService.getReviewsByMovieId(movieId);

        if (!reviews.isEmpty()) {
            movieToUpdate.setRating(EntityHelper.recountRating(reviews, rating));
            movieService.updateMovie(movieToUpdate);
        } else {
            movieToUpdate.setRating(Double.valueOf(rating));
            movieService.updateMovie(movieToUpdate);
        }
    }



    // ********--------********

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

}
