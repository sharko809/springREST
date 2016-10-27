package com.serviceapp.controller;

import com.serviceapp.entity.Movie;
import com.serviceapp.entity.Review;
import com.serviceapp.entity.dto.ReviewTransferObject;
import com.serviceapp.entity.util.MovieContainer;
import com.serviceapp.exception.OnGetNullException;
import com.serviceapp.security.securityEntity.UserDetailsImpl;
import com.serviceapp.service.MovieService;
import com.serviceapp.service.ReviewService;
import com.serviceapp.service.UserService;
import com.serviceapp.util.EntityHelper;
import com.serviceapp.util.PrincipalUtil;
import com.serviceapp.util.ResponseErrorHelper;
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
    private static final Integer RECORDS_PER_PAGE = 6;
    private MovieService movieService;
    private ReviewService reviewService;
    private UserService userService;

    @Autowired
    public MovieController(MovieService movieService, ReviewService reviewService, UserService userService) {
        this.movieService = movieService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    /**
     * Get paged list of movies
     *
     * @param pageable <code>org.springframework.data.domain.Pageable</code> for convenient pagination and sorting
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if movies retrieved successfully. Body will be a paged movies list</li>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity paged(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() < 0 ? 0 : pageable.getPageNumber();
        Page<Movie> movies = movieService.findAllPaged(new PageRequest(pageNumber, RECORDS_PER_PAGE, null));
        if (movies.getTotalPages() - 1 < pageNumber) {
            return ResponseErrorHelper
                    .responseError(HttpStatus.NOT_FOUND, "Sorry, last page is " + (movies.getTotalPages() - 1));
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    /**
     * Get movie data
     *
     * @param movieId id of movie to get
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * <li>200 - if movie retrieved successfully</li>
     * <li>404 - if no movie has been found</li>
     * <li>500 - if some internal error that can't be handled at once occurred (primarily some severe db errors)</li>
     */
    @RequestMapping(value = "/{movieId}", method = RequestMethod.GET)
    public ResponseEntity movie(@PathVariable Long movieId) {
        if (!movieService.movieExists(movieId)) {
            return ResponseErrorHelper.responseError(HttpStatus.NOT_FOUND, "No such movie found");
        }

        MovieContainer container;
        try {
            container = EntityHelper.completeMovie(movieId, movieService, reviewService, userService);
        } catch (OnGetNullException e) {
            LOGGER.error("Unable to get movie", e);
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage() + " Some internal problems occurred");
        }
        return new ResponseEntity<>(container, HttpStatus.OK);
    }

    /**
     * Performs review posting and accompanied functions (recalculating movie rating)
     *
     * @param movieId      path variable - id of movie for which review is written
     * @param reviewObject object holding review data (title, text, rating)
     * @param errors       errors generated if <code>reviewObject</code> param failed validation
     * @return status code representing the status of the operation:
     * <li><code>204</code> - if no review content has been provided</li>
     * <li><code>400</code> - if review data failed validation</li>
     * <li><code>500</code> - if review has not been created (to some unexpected reasons)</li>
     * <li><code>200</code> - if review has been created successfully</li>
     */
    @RequestMapping(value = "/{movieId}/post", method = RequestMethod.POST)
    public ResponseEntity postReview(@PathVariable Long movieId,
                                     @Validated @RequestBody(required = false) ReviewTransferObject reviewObject,
                                     BindingResult errors) {
        if (!movieService.movieExists(movieId)) {
            return ResponseErrorHelper.responseError(HttpStatus.NOT_FOUND, "No such movie found");
        }
        if (errors.hasErrors()) {
            List<String> validationErrors = errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseErrorHelper.responseError(HttpStatus.BAD_REQUEST, validationErrors);
        }

        UserDetailsImpl currentUser = PrincipalUtil.getCurrentPrincipal();
        if (currentUser == null) {
            LOGGER.error("No authentication detected");
            return ResponseErrorHelper.responseError(HttpStatus.FORBIDDEN, "No authentication detected");
        }
        Date postDate = new Date(new java.util.Date().getTime());

        Review review = EntityHelper.dtoToReview(reviewObject);
        review.setMovieId(movieId);
        review.setUserId(currentUser.getId());
        review.setPostDate(postDate);
        Review createdReview = reviewService.createReview(review);
        if (createdReview == null) {
            LOGGER.error("Review is not created. Please, see all logs for details");
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "Review is not created");
        }
        try {
            Movie updated = updateMovieRating(movieId, reviewObject.getRating());
            if (updated == null) {
                LOGGER.error("Rating not updated. Please, see all logs for details");
                return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "Rating not updated");
            }
        } catch (OnGetNullException e) {
            LOGGER.error("Rating not updated", e);
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "Rating not updated", e);
        }
        MovieContainer container;
        try {
            container = EntityHelper.completeMovie(movieId, movieService, reviewService, userService);
        } catch (OnGetNullException e) {
            LOGGER.error("Unable to get movie", e);
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage() + " Some internal problems occurred");
        }
        return new ResponseEntity<>(container, HttpStatus.OK);
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
     * @return updated <code>Movie</code> object
     */
    private Movie updateMovieRating(Long movieId, Integer rating) throws OnGetNullException {
        Movie movieToUpdate;
        List<Review> reviews;
        try {
            movieToUpdate = movieService.getMovie(movieId);
            reviews = reviewService.getReviewsByMovieId(movieId);
        } catch (Exception e) {
            throw new OnGetNullException(e);
        }

        if (!reviews.isEmpty()) {
            movieToUpdate.setRating(EntityHelper.recountRating(reviews, rating));
            return movieService.updateMovie(movieToUpdate);
        } else {
            movieToUpdate.setRating(Double.valueOf(rating));
            return movieService.updateMovie(movieToUpdate);
        }
    }

}
