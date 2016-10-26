package com.serviceapp.controller;

import com.serviceapp.entity.Movie;
import com.serviceapp.entity.Review;
import com.serviceapp.entity.User;
import com.serviceapp.entity.dto.MovieTransferObject;
import com.serviceapp.entity.dto.UserTransferObject;
import com.serviceapp.entity.util.MovieContainer;
import com.serviceapp.entity.util.SortTypeUser;
import com.serviceapp.exception.OnGetNullException;
import com.serviceapp.security.PasswordManager;
import com.serviceapp.security.UserDetailsImpl;
import com.serviceapp.service.MovieService;
import com.serviceapp.service.ReviewService;
import com.serviceapp.service.UserService;
import com.serviceapp.util.EntityHelper;
import com.serviceapp.util.PrincipalUtil;
import com.serviceapp.util.ResponseErrorHelper;
import com.serviceapp.validation.marker.CreateUserValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This controller handles all admin activity
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger LOGGER = LogManager.getLogger();
    /**
     * Default user sorting value in users page
     */
    private static final String DEFAULT_USER_SORT_TYPE = "id";
    /**
     * User records per page
     */
    private static final Integer USER_RECORDS_PER_PAGE = 10;
    /**
     * Movie records per page
     */
    private static final Integer MOVIE_RECORDS_PER_PAGE = 10;
    private MovieService movieService;
    private ReviewService reviewService;
    private UserService userService;
    private PasswordManager passwordManager;

    @Autowired
    public AdminController(MovieService movieService, ReviewService reviewService, UserService userService,
                           PasswordManager passwordManager) {
        this.movieService = movieService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.passwordManager = passwordManager;
    }

    /**
     * Create new movie
     *
     * @param movie  <code>MovieTransferObject</code> populated with movie data
     * @param errors errors generated if validation failed
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if movie created successfully</li>
     * <li>400 - if there were errors in movie data</li>
     * <li>422 - if no object with movie data could be detected</li>
     * <li>500 - if some internal error that can't be handled at once occurred (primarily some severe db errors)</li>
     */
    @RequestMapping(value = "/addmovie", method = RequestMethod.POST)
    public ResponseEntity addMovie(@Validated @RequestBody(required = false) MovieTransferObject movie,
                                   BindingResult errors) {
        if (movie == null) {
            LOGGER.warn("No movie data detected in the request");
            return ResponseErrorHelper.responseError(HttpStatus.UNPROCESSABLE_ENTITY, "No movie data detected");
        }
        if (errors.hasErrors()) {
            List<String> validationErrors = errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseErrorHelper.responseError(HttpStatus.BAD_REQUEST, validationErrors);
        }

        movie.setRating(0D);
        Movie movieToAdd = EntityHelper.dtoToMovie(movie);
        Movie added = movieService.createMovie(movieToAdd);
        if (added == null) {
            LOGGER.error("Unable to create movie. See all logs for details");
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create movie");
        }

        return new ResponseEntity<>("Movie " + added.getMovieName() + " created successfully", HttpStatus.OK);
    }

    /**
     * Get paged list of movies
     *
     * @param pageable <code>org.springframework.data.domain.Pageable</code> for convenient pagination and sorting
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if movies retrieved successfully. Body will be a paged movies list</li>
     */
    @RequestMapping(value = "/managemovies", method = RequestMethod.GET)
    public ResponseEntity manageMovies(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() < 0 ? 0 : pageable.getPageNumber();
        Page<Movie> movies = movieService.findAllPaged(new PageRequest(pageNumber, MOVIE_RECORDS_PER_PAGE, null));
        if (movies.getTotalPages() - 1 < pageNumber) {
            return ResponseErrorHelper
                    .responseError(HttpStatus.NOT_FOUND, "Sorry, last page is " + (movies.getTotalPages() - 1));
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    /**
     * Recalculates movie rating. Movie rating is based on user rating sent with reviews. If no reviews found for
     * particular movie - default <code>0</code> value is set.
     *
     * @param movieId id of movie to recalculate rating
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if movie rating updated successfully</li>
     * <li>404 - if no movie to update has been found</li>
     * <li>500 - if some internal error that can't be handled at once occurred (primarily some severe db errors)</li>
     */
    @RequestMapping(value = "/managemovies", method = RequestMethod.PUT)
    public ResponseEntity updRating(@RequestParam Long movieId) {
        if (!movieService.movieExists(movieId)) {
            return ResponseErrorHelper.responseError(HttpStatus.NOT_FOUND, "Can't find movie to update rating");
        }

        Movie movieToUpdate = movieService.getMovie(movieId);
        if (movieToUpdate == null) {
            LOGGER.error("Unable to update movie. See all logs for details");
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update movie");
        }

        List<Review> reviews = reviewService.getReviewsByMovieId(movieId);
        if (reviews.isEmpty()) {
            movieToUpdate.setRating(0d);
            LOGGER.warn("No reviews found for movie " + movieToUpdate.getMovieName() + " with id "
                    + movieToUpdate.getId() + ". So rating can't be updated. Rating set to 0.");
        } else {
            movieToUpdate.setRating(EntityHelper.recountRating(reviews));
        }

        Movie updated = movieService.updateMovie(movieToUpdate);
        if (updated == null) {
            LOGGER.error("Unable to update movie. Please, see all logs for details");
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update movie");
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Get movie data
     *
     * @param movieId id of movie to get
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * <li>200 - if movie retrieved successfully</li>
     * <li>404 - if no movie has been found</li>
     */
    @RequestMapping(value = "/managemovies/{movieId}", method = RequestMethod.GET)
    public ResponseEntity movieToEdit(@PathVariable Long movieId) {
        if (!movieService.movieExists(movieId)) {
            return ResponseErrorHelper.responseError(HttpStatus.NOT_FOUND, "No such movie found");
        }

        MovieContainer movieContainer;
        try {
            movieContainer = EntityHelper.completeMovie(movieId, movieService, reviewService, userService);
        } catch (OnGetNullException e) {
            LOGGER.error("Unable to get movie", e);
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return new ResponseEntity<>(movieContainer, HttpStatus.OK);
    }

    /**
     * Updated movie data
     *
     * @param movieId id of movie to update
     * @param movie   <code>MovieTransferObject</code> populated with movie data
     * @param errors  errors generated if validation failed
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if movie updated successfully</li>
     * <li>400 - if there were errors in movie data</li>
     * <li>404 - if no movie to update has been found</li>
     * <li>422 - if no object with user data could be detected</li>
     * <li>500 - if some internal error that can't be handled at once occurred (primarily some severe db errors)</li>
     */
    @RequestMapping(value = "/managemovies/{movieId}", method = RequestMethod.PUT)
    public ResponseEntity editMovie(@PathVariable Long movieId,
                                    @Validated @RequestBody(required = false) MovieTransferObject movie,
                                    BindingResult errors) {
        if (movie == null) {
            LOGGER.warn("No movie data detected in the request");
            return ResponseErrorHelper.responseError(HttpStatus.UNPROCESSABLE_ENTITY, "No movie data detected");
        }
        if (!movieService.movieExists(movieId)) {
            return ResponseErrorHelper.responseError(HttpStatus.NOT_FOUND, "No movie with such id found");
        }
        if (errors.hasErrors()) {
            List<String> validationErrors = errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseErrorHelper.responseError(HttpStatus.BAD_REQUEST, validationErrors);
        }

        Movie movieToUpdate = movieService.getMovie(movieId);
        if (movieToUpdate == null) {
            LOGGER.error("Can't find movie to update. Please, see all logs for details");
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "Can't find movie to update");
        }
        Movie updated = movieService.updateMovie(EntityHelper.updateMovieFields(movieToUpdate, movie));

        return new ResponseEntity<>("Movie " + updated.getMovieName() + " updated", HttpStatus.OK);
    }

    /**
     * Deletes review
     *
     * @param reviewId id of review to delete
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if review deleted successfully</li>
     * <li>404 - if no review found with provided id</li>
     * <li>500 - if some internal error that can't be handled at once occurred (primarily some severe db errors)</li>
     */
    @RequestMapping(value = "/delreview", method = RequestMethod.DELETE)
    public ResponseEntity deleteReview(@RequestParam Long reviewId) {
        if (!reviewService.reviewExists(reviewId)) {
            return ResponseErrorHelper.responseError(HttpStatus.NOT_FOUND, "No review to delete found");
        }

        Review reviewToDelete = reviewService.getReview(reviewId);
        if (reviewToDelete == null) {
            LOGGER.error("No review to delete found. Please, see all logs for details");
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "No review to delete found");
        }
        reviewService.deleteReview(reviewToDelete);

        return new ResponseEntity<>("Review deleted", HttpStatus.OK);
    }

    /**
     * Get paged users list
     *
     * @param pageable <code>org.springframework.data.domain.Pageable</code> for convenient pagination and sorting
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * <li>200 - if users retrieved successfully. Body will be a pageable users list</li>
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity users(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() < 0 ? 0 : pageable.getPageNumber();
        Sort sort = pageable.getSort();
        if (sort == null) {
            sort = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_USER_SORT_TYPE));
        } else {
            for (Sort.Order order : sort) {
                if (!SortTypeUser.isUserSortType(order.getProperty())) {
                    sort = order.withProperties(DEFAULT_USER_SORT_TYPE);
                }
            }
        }

        Page<User> users = userService.getAllUsersPaged(new PageRequest(pageNumber, USER_RECORDS_PER_PAGE, sort));
        if (users.getTotalPages() - 1 < pageNumber) {
            return ResponseErrorHelper
                    .responseError(HttpStatus.NOT_FOUND, "Sorry, last page is " + (users.getTotalPages() - 1));
        }
        users.forEach(user -> user.setPassword(null));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Make user admin (grant admin authorities)
     *
     * @param userId id of user to make admin
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if user became admin successfully</li>
     * <li>404 - if no user found with provided id</li>
     * <li>409 - if attempted to change own admin state</li>
     * <li>500 - if some internal error that can't be handled at once occurred (primarily some severe db errors)</li>
     */
    @RequestMapping(value = "/adminize", method = RequestMethod.PUT)
    public ResponseEntity makeAdmin(@RequestParam Long userId) {
        if (!userService.userExists(userId)) {
            LOGGER.warn("No user with such id");
            return ResponseErrorHelper.responseError(HttpStatus.NOT_FOUND, "No user with such id");
        }

        User userToUpdate = userService.getUser(userId);
        if (userToUpdate == null) {
            LOGGER.error("Unable to find user to make admin. See all logs for details");
            return ResponseErrorHelper
                    .responseError(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find user to make admin");
        }
        UserDetailsImpl currentUser = PrincipalUtil.getCurrentPrincipal();
        if (currentUser == null) {
            LOGGER.error("No authentication detected");
            return ResponseErrorHelper.responseError(HttpStatus.FORBIDDEN, "No authentication detected");
        }
        if (currentUser.getId().equals(userToUpdate.getId())) {
            return ResponseErrorHelper.responseError(HttpStatus.CONFLICT, "Can't change your own admin state");
        }
        userToUpdate.setAdmin(!userToUpdate.isAdmin());
        User updated = userService.updateUser(userToUpdate);
        if (updated == null) {
            LOGGER.error("User has not been updated. Please, see all logs for details");
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "User has not been updated");
        }

        return new ResponseEntity<>("User " + updated.getLogin() + " is admin now", HttpStatus.OK);
    }

    /**
     * Ban user
     *
     * @param userId id of user to ban
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if user banned successfully</li>
     * <li>404 - if no user found with provided id</li>
     * <li>409 - if attempted to ban yourself</li>
     * <li>500 - if some internal error that can't be handled at once occurred (primarily some severe db errors)</li>
     */
    @RequestMapping(value = "/ban", method = RequestMethod.PUT)
    public ResponseEntity banUser(@RequestParam Long userId) {
        if (!userService.userExists(userId)) {
            LOGGER.error("No user with such id");
            return ResponseErrorHelper.responseError(HttpStatus.NOT_FOUND, "No user with such id");
        }

        User userToUpdate = userService.getUser(userId);
        if (userToUpdate == null) {
            LOGGER.error("Unable to find user to ban. Please, see logs for details");
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to ban user");
        }
        UserDetailsImpl currentUser = PrincipalUtil.getCurrentPrincipal();
        if (currentUser == null) {
            LOGGER.error("No authentication detected");
            return ResponseErrorHelper.responseError(HttpStatus.FORBIDDEN, "No authentication detected");
        }
        if (currentUser.getId().equals(userToUpdate.getId())) {
            return ResponseErrorHelper.responseError(HttpStatus.CONFLICT, "Can't ban yourself");
        }
        userToUpdate.setBanned(!userToUpdate.isBanned());
        User updated = userService.updateUser(userToUpdate);
        if (updated == null) {
            LOGGER.error("User has not been updated. See all logs for details");
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "User has not been updated");
        }

        return new ResponseEntity<>("User " + updated.getLogin() + " banned", HttpStatus.OK);
    }

    /**
     * Create new user
     *
     * @param user   <code>UserTransferObject</code> populated with user data
     * @param errors errors generated if validation failed
     * @return <code>ResponseEntity</code> with content (body and http status) depending on events occurred.
     * Status codes:
     * <li>200 - if user created</li>
     * <li>400 - if there were errors in user data</li>
     * <li>409 - if attempted to create user with existing login</li>
     * <li>422 - if no object with user data could be detected</li>
     * <li>500 - if some internal error that can't be handled at once occurred (primarily some severe db errors)</li>
     */
    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public ResponseEntity addNewUser(@Validated({Default.class, CreateUserValidation.class})
                                     @RequestBody(required = false) UserTransferObject user, BindingResult errors) {
        if (user == null) {
            LOGGER.warn("No user data detected in the request");
            return ResponseErrorHelper.responseError(HttpStatus.UNPROCESSABLE_ENTITY, "No user data detected");
        }
        if (errors.hasErrors()) {
            List<String> validationErrors = errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseErrorHelper.responseError(HttpStatus.BAD_REQUEST, validationErrors);
        }

        Boolean userExists = userService.getUserByLogin(user.getLogin()) != null;
        if (userExists) {
            return ResponseErrorHelper.responseError(HttpStatus.CONFLICT, "User with such login already exists");
        }
        String encodedPassword = passwordManager.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setBanned(false);
        User created = userService.createUser(EntityHelper.dtoToUser(user));
        if (created == null) {
            LOGGER.error("User has not been created. See all logs for details");
            return ResponseErrorHelper.responseError(HttpStatus.INTERNAL_SERVER_ERROR, "User has not been created");
        }

        String success = "User <b>" + created.getLogin() + "</b> created successfully";
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

}
