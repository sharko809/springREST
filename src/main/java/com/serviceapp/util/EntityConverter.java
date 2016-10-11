package com.serviceapp.util;

import com.serviceapp.entity.Movie;
import com.serviceapp.entity.Review;
import com.serviceapp.entity.User;
import com.serviceapp.entity.dto.MovieTransferObject;
import com.serviceapp.entity.dto.ReviewTransferObject;
import com.serviceapp.entity.dto.UserShortDto;
import com.serviceapp.entity.dto.UserTransferObject;

/**
 * Helper class to convert DTO to entities and backwards.
 */
public class EntityConverter {

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
        userTransferObject.setName(user.getName());
        userTransferObject.setLogin(user.getLogin());
        return userTransferObject;
    }

    public static MovieTransferObject movieToDto(Movie movie) {
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

}
