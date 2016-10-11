package com.serviceapp.controller;

import com.serviceapp.entity.ErrorEntity;
import com.serviceapp.entity.Movie;
import com.serviceapp.entity.Review;
import com.serviceapp.entity.User;
import com.serviceapp.entity.dto.MovieTransferObject;
import com.serviceapp.entity.dto.UserTransferObject;
import com.serviceapp.entity.util.MovieContainer;
import com.serviceapp.entity.util.SortTypeUser;
import com.serviceapp.security.PasswordManager;
import com.serviceapp.service.MovieService;
import com.serviceapp.service.ReviewService;
import com.serviceapp.service.UserService;
import com.serviceapp.util.EntityHelper;
import com.serviceapp.util.PrincipalUtil;
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
    private static final Integer U_RECORDS_PER_PAGE = 10;
    /**
     * Movie records per page
     */
    private static final Integer M_RECORDS_PER_PAGE = 10;
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

    @RequestMapping(value = "/addmovie", method = RequestMethod.POST)
    public ResponseEntity addMovie(@Validated @RequestBody(required = false) MovieTransferObject movie,
                                   BindingResult errors) {
        // TODO
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/managemovies", method = RequestMethod.GET)
    public ResponseEntity manageMovies(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() < 0 ? 0 : pageable.getPageNumber();
        return new ResponseEntity<>(
                movieService.findAllPaged(new PageRequest(pageNumber, M_RECORDS_PER_PAGE, null)), HttpStatus.OK);
    }

    @RequestMapping(value = "/managemovies", method = RequestMethod.POST)
    public ResponseEntity updRating(@RequestParam Long movieId) {
        if (!movieService.ifMovieExists(movieId)) {
            ErrorEntity error = new ErrorEntity(HttpStatus.NOT_FOUND, "Can't find movie to update rating");
            return new ResponseEntity<>(error, error.getStatus());
        }

        Movie movieToUpdate = movieService.getMovie(movieId);
        if (movieToUpdate == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update movie");
            return new ResponseEntity<>(error, error.getStatus());
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
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update movie");
            return new ResponseEntity<>(error, error.getStatus());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/managemovies/{id}", method = RequestMethod.GET)
    public ResponseEntity movieToEdit(@PathVariable Long id) {
        if (!movieService.ifMovieExists(id)) {
            ErrorEntity error = new ErrorEntity(HttpStatus.NOT_FOUND, "No such movie found");
            return new ResponseEntity<>(error, error.getStatus());
        }

        MovieContainer movieContainer = EntityHelper.completeMovie(id, movieService, reviewService, userService);
        return new ResponseEntity<>(movieContainer, HttpStatus.OK);
    }

    @RequestMapping(value = "/managemovies/{id}", method = RequestMethod.POST)
    public ResponseEntity editMovie(@PathVariable Long id,
                                    @Validated @RequestBody(required = false) MovieTransferObject movie,
                                    BindingResult errors) {
        if (movie == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.UNPROCESSABLE_ENTITY, "No movie data detected");
            return new ResponseEntity<>(error, error.getStatus());
        }
        if (!movieService.ifMovieExists(id)) {
            ErrorEntity error = new ErrorEntity(HttpStatus.NOT_FOUND, "No movie with such id found");
            return new ResponseEntity<>(error, error.getStatus());
        }
        if (errors.hasErrors()) {
            List<String> validationErrors = errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            ErrorEntity error = new ErrorEntity(HttpStatus.BAD_REQUEST, validationErrors);
            return new ResponseEntity<>(error, error.getStatus());
        }

        Movie movieToUpdate = movieService.getMovie(id);
        if (movieToUpdate == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Can't find movie to update");
            return new ResponseEntity<>(error, error.getStatus());
        }
        Movie updated = movieService.updateMovie(EntityHelper.updateMovieFields(movieToUpdate, movie));

        return new ResponseEntity<>("Movie " + updated.getMovieName() + " updated", HttpStatus.OK);
    }

    @RequestMapping(value = "/delreview", method = RequestMethod.POST)
    public ResponseEntity deleteReview(@RequestParam Long id) {
        if (!reviewService.ifReviewExists(id)) {
            ErrorEntity error = new ErrorEntity(HttpStatus.NOT_FOUND, "No review to delete found");
            return new ResponseEntity<>(error, error.getStatus());
        }

        Review reviewToDelete = reviewService.getReview(id);
        if (reviewToDelete == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "No review to delete found");
            return new ResponseEntity<>(error, error.getStatus());
        }
        reviewService.deleteReview(reviewToDelete);

        return new ResponseEntity<>("Review deleted", HttpStatus.OK);
    }

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

        Page<User> users = userService.getAllUsersPaged(new PageRequest(pageNumber, U_RECORDS_PER_PAGE, sort));
        users.forEach(user -> user.setPassword(null));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/adminize", method = RequestMethod.POST)
    public ResponseEntity makeAdmin(@RequestParam Long userId) {
        if (!userService.ifUserExists(userId)) {
            ErrorEntity error = new ErrorEntity(HttpStatus.NOT_FOUND, "No user with such id");
            return new ResponseEntity<>(error, error.getStatus());
        }

        User userToUpdate = userService.getUser(userId);
        if (userToUpdate == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find user to make admin");
            return new ResponseEntity<>(error, error.getStatus());
        }
        Long currentUserId = PrincipalUtil.getCurrentPrincipal().getId();
        if (currentUserId.equals(userToUpdate.getId())) {
            ErrorEntity error = new ErrorEntity(HttpStatus.CONFLICT, "Can't change your own admin state");// TODO code?
            return new ResponseEntity<>(error, error.getStatus());
        }
        userToUpdate.setBanned(!userToUpdate.isBanned());
        User updated = userService.updateUser(userToUpdate);
        if (updated == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "User has not been updated");
            return new ResponseEntity<>(error, error.getStatus());
        }

        return new ResponseEntity<>("User " + updated.getLogin() + " is admin now", HttpStatus.OK);
    }

    @RequestMapping(value = "/ban", method = RequestMethod.POST)
    public ResponseEntity banUser(@RequestParam Long userId) {
        if (!userService.ifUserExists(userId)) {
            ErrorEntity error = new ErrorEntity(HttpStatus.NOT_FOUND, "No user with such id");
            return new ResponseEntity<>(error, error.getStatus());
        }

        User userToUpdate = userService.getUser(userId);
        if (userToUpdate == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find user to ban");
            return new ResponseEntity<>(error, error.getStatus());
        }
        Long currentUserId = PrincipalUtil.getCurrentPrincipal().getId();
        if (currentUserId.equals(userToUpdate.getId())) {
            ErrorEntity error = new ErrorEntity(HttpStatus.CONFLICT, "Can't ban yourself");// TODO code?
            return new ResponseEntity<>(error, error.getStatus());
        }
        userToUpdate.setAdmin(!userToUpdate.isAdmin());
        User updated = userService.updateUser(userToUpdate);
        if (updated == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "User has not been updated");
            return new ResponseEntity<>(error, error.getStatus());
        }

        return new ResponseEntity<>("User " + updated.getLogin() + " banned", HttpStatus.OK);
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public ResponseEntity addNewUser(@Validated({Default.class, CreateUserValidation.class})
                                     @RequestBody(required = false) UserTransferObject user, BindingResult errors) {
        if (user == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.UNPROCESSABLE_ENTITY, "No user data detected");
            return new ResponseEntity<>(error, error.getStatus());
        }
        if (errors.hasErrors()) {
            List<String> validationErrors = errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            ErrorEntity error = new ErrorEntity(HttpStatus.BAD_REQUEST, validationErrors);
            return new ResponseEntity<>(error, error.getStatus());
        }

        Boolean userExists = userService.getUserByLogin(user.getLogin()) != null;
        if (userExists) {
            ErrorEntity error = new ErrorEntity(HttpStatus.CONFLICT, "User with such login already exists");
            return new ResponseEntity<>(error, error.getStatus());
        }
        String encodedPassword = passwordManager.encode(user.getPassword());
        user.setPassword(encodedPassword);
//            user.setAdmin(user.getAdmin()); // TODO check if it works ok
        user.setBanned(false);
        User created = userService.createUser(EntityHelper.dtoToUser(user));
        if (created == null) {
            ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, "User has not been created");
            return new ResponseEntity<>(error, error.getStatus());
        }

        String success = "User <b>" + created.getLogin() + "</b> created successfully";
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

}
