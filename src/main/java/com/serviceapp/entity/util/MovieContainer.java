package com.serviceapp.entity.util;

import com.serviceapp.entity.Review;
import com.serviceapp.entity.dto.MovieTransferObject;

import java.util.List;
import java.util.Map;

/**
 * Class stores all movie-related data
 */
public class MovieContainer {

    private MovieTransferObject movieTransferObject;
    private List<Review> reviews;
    private Map<Long, Object> users;

    public MovieTransferObject getMovieTransferObject() {
        return movieTransferObject;
    }

    public void setMovieTransferObject(MovieTransferObject movieTransferObject) {
        this.movieTransferObject = movieTransferObject;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Map<Long, Object> getUsers() {
        return users;
    }

    public void setUsers(Map<Long, Object> users) {
        this.users = users;
    }
}
