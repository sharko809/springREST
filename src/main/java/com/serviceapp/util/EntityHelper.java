package com.serviceapp.util;

import com.serviceapp.entity.Movie;
import com.serviceapp.entity.Review;
import com.serviceapp.entity.User;
import com.serviceapp.entity.dto.MovieTransferObject;
import com.serviceapp.entity.dto.ReviewTransferObject;
import com.serviceapp.entity.dto.UserShortDto;
import com.serviceapp.entity.dto.UserTransferObject;
import com.serviceapp.entity.util.MovieContainer;
import com.serviceapp.exception.OnGetNullException;
import com.serviceapp.service.MovieService;
import com.serviceapp.service.ReviewService;
import com.serviceapp.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class to convert DTO to entities and backwards.
 */
public class EntityHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Converts provided <code>UserTransferObject</code> to <code>User</code> object
     *
     * @param userTransferObject <code>UserTransferObject</code> to convert to <code>User</code>
     * @return <code>User</code> object with fields populated from provided <code>UserTransferObject</code>. If
     * <code>userTransferObject</code> parameter is <code>null</code> - returns <code>null</code>
     * @see UserTransferObject
     * @see User
     */
    public static User dtoToUser(UserTransferObject userTransferObject) {
        if (userTransferObject == null) {
            return null;
        }
        User user = new User();
        user.setName(userTransferObject.getName());
        user.setLogin(userTransferObject.getLogin());
        user.setPassword(userTransferObject.getPassword());
        user.setAdmin(userTransferObject.getAdmin());
        user.setBanned(userTransferObject.getBanned());
        return user;
    }

    /**
     * Converts provided <code>ReviewTransferObject</code> to <code>Review</code> object
     *
     * @param reviewTransferObject <code>ReviewTransferObject</code> to convert to <code>Review</code>
     * @return <code>Review</code> object with fields populated from provided <code>ReviewTransferObject</code>. If
     * <code>reviewTransferObject</code> parameter is <code>null</code> - returns <code>null</code>
     * @see ReviewTransferObject
     * @see Review
     */
    public static Review dtoToReview(ReviewTransferObject reviewTransferObject) {
        if (reviewTransferObject == null) {
            return null;
        }
        Review review = new Review();
        review.setTitle(reviewTransferObject.getTitle());
        review.setRating(reviewTransferObject.getRating());
        review.setReviewText(reviewTransferObject.getText());
        return review;
    }

    /**
     * Converts provided <code>MovieTransferObject</code> to <code>Movie</code> object
     *
     * @param movieTransferObject <code>MovieTransferObject</code> to convert to <code>Movie</code>
     * @return <code>Movie</code> object with fields populated from provided <code>MovieTransferObject</code>. If
     * <code>movieTransferObject</code> parameter is <code>null</code> - returns <code>null</code>
     * @see MovieTransferObject
     * @see Movie
     */
    public static Movie dtoToMovie(MovieTransferObject movieTransferObject) {
        if (movieTransferObject == null) {
            return null;
        }
        Movie movie = new Movie();
        movie.setId(movieTransferObject.getId());
        movie.setMovieName(movieTransferObject.getMovieName());
        movie.setDirector(movieTransferObject.getDirector());
        movie.setReleaseDate(movieTransferObject.getReleaseDate());
        movie.setRating(movieTransferObject.getRating());
        movie.setPosterURL(movieTransferObject.getPosterURL());
        movie.setTrailerURL(movieTransferObject.getTrailerURL());
        movie.setDescription(movieTransferObject.getDescription());
        return movie;
    }

    /**
     * Converts provided <code>User</code> to <code>UserShortDto</code> object
     *
     * @param user <code>User</code> to convert to <code>UserShortDto</code>
     * @return <code>UserShortDto</code> object with fields populated from provided <code>User</code>. If
     * <code>user</code> parameter is <code>null</code> - returns <code>null</code>
     * @see UserShortDto
     * @see User
     */
    public static UserShortDto userToDtoShort(User user) {
        if (user == null) {
            return null;
        }
        UserShortDto userTransferObject = new UserShortDto();
        userTransferObject.setId(user.getId());
        userTransferObject.setName(user.getName());
        userTransferObject.setLogin(user.getLogin());
        return userTransferObject;
    }

    /**
     * Converts provided <code>Movie</code> to <code>MovieTransferObject</code> object
     *
     * @param movie <code>Movie</code> to convert to <code>MovieTransferObject</code>
     * @return <code>MovieTransferObject</code> object with fields populated from provided <code>Movie</code>. If
     * <code>movie</code> parameter is <code>null</code> - returns <code>null</code>
     * @see MovieTransferObject
     * @see Movie
     */
    private static MovieTransferObject movieToDto(Movie movie) {
        if (movie == null) {
            return null;
        }
        MovieTransferObject movieTransferObject = new MovieTransferObject();
        movieTransferObject.setId(movie.getId());
        movieTransferObject.setMovieName(movie.getMovieName());
        movieTransferObject.setDirector(movie.getDirector());
        movieTransferObject.setReleaseDate(movie.getReleaseDate());
        movieTransferObject.setTrailerURL(movie.getTrailerURL());
        movieTransferObject.setPosterURL(movie.getPosterURL());
        movieTransferObject.setRating(movie.getRating());
        movieTransferObject.setDescription(movie.getDescription());
        return movieTransferObject;
    }

    /**
     * Helper method to get and pack movie data, users and their reviews into one single <code>MovieContainer</code>
     * object for convenience.
     *
     * @param movieId id of movie for witch to retrieve data
     * @return <code>MovieContainer</code> object with all movie-related data
     * @throws OnGetNullException if exception occurred reading from database
     * @see MovieContainer
     */
    public static MovieContainer completeMovie(Long movieId, MovieService movieService, ReviewService reviewService,
                                               UserService userService) throws OnGetNullException {
        MovieContainer movieContainer = new MovieContainer();
        Movie movie = movieService.getMovie(movieId);
        if (movie == null) {
            throw new OnGetNullException("Unable to get movie");
        }
        MovieTransferObject movieTransferObject = EntityHelper.movieToDto(movie);
        List<Review> reviews = reviewService.getReviewsByMovieId(movieId);
        reviews.sort((r1, r2) -> r2.getPostDate().compareTo(r1.getPostDate()));
        List<UserShortDto> users = new ArrayList<>();
        if (reviews.size() > 0) {
            for (Review review : reviews) {
                if (review != null)
                    if (review.getUserId() != null)
                        if (review.getUserId() > 0) {
                            User user = userService.getUser(review.getUserId());
                            if (user == null) {
                                throw new OnGetNullException("Unable to get user");
                            }
                            UserShortDto userShort = EntityHelper.userToDtoShort(user);
                            userShort.setLogin(null);
                            users.add(userShort);
                        }
            }
        }
        movieContainer.setMovieTransferObject(movieTransferObject);
        movieContainer.setReviews(reviews);
        movieContainer.setUsers(users);
        return movieContainer;
    }

    /**
     * Populates given movie object with new data from updated movie transfer object
     *
     * @param movieToUpdate movie to be updated
     * @param updatedMovie  updated movie info (movie transfer object)
     * @return <code>Movie</code> object with data populated from updated movie transfer object
     * @see MovieTransferObject
     * @see Movie
     */
    public static Movie updateMovieFields(Movie movieToUpdate, MovieTransferObject updatedMovie) {
        if (movieToUpdate == null || updatedMovie == null) {
            return null;
        }
        movieToUpdate.setMovieName(updatedMovie.getMovieName());
        movieToUpdate.setDirector(updatedMovie.getDirector());
        movieToUpdate.setReleaseDate(updatedMovie.getReleaseDate());
        movieToUpdate.setPosterURL(updatedMovie.getPosterURL());
        movieToUpdate.setTrailerURL(updatedMovie.getTrailerURL());
        movieToUpdate.setDescription(updatedMovie.getDescription());
        return movieToUpdate;
    }

    /**
     * Recounts current movie rating taking in account new rating
     *
     * @param reviews <code>List</code> of <code>Review</code> objects to be parsed for ratings
     * @param rating  new rating to add to summary
     * @return new rating value
     */
    public static Double recountRating(List<Review> reviews, Integer rating) {
        if (rating == null || rating < 0) {
            rating = 0;
        }
        Double totalRating = 0d;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        char separator = ((DecimalFormat) DecimalFormat.getInstance()).getDecimalFormatSymbols().getDecimalSeparator();
        DecimalFormat df = new DecimalFormat("#" + separator + "##");
        Double newRating = (totalRating + rating) / (reviews.size() + 1);
        Double newRatingFormatted = null;
        try {
            newRatingFormatted = Double.valueOf(df.format(newRating));
        } catch (NumberFormatException e) {
            LOGGER.error("Error parsing rating during movie rating update.", e);
        }
        return (newRatingFormatted != null) ? newRatingFormatted : newRating;
    }

    /**
     * Recounts current movie rating based on review ratings
     *
     * @param reviews <code>List</code> of <code>Review</code> objects to be parsed for ratings
     * @return recalculated rating value as <code>Double</code>
     */
    public static Double recountRating(List<Review> reviews) {
        Double totalRating = 0d;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        char separator = ((DecimalFormat) DecimalFormat.getInstance()).getDecimalFormatSymbols().getDecimalSeparator();
        DecimalFormat df = new DecimalFormat("#" + separator + "##");
        Double newRating = totalRating / reviews.size();
        try {
            newRating = Double.valueOf(df.format(newRating));
        } catch (NumberFormatException e) {
            LOGGER.error("Error parsing rating during movie rating update.", e);
        }
        return newRating;
    }


}
