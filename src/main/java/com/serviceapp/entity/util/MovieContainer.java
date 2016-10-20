package com.serviceapp.entity.util;

import com.serviceapp.entity.Review;
import com.serviceapp.entity.dto.MovieTransferObject;
import com.serviceapp.entity.dto.UserShortDto;

import java.util.List;

/**
 * Class stores all movie-related data
 */
public class MovieContainer {

    private MovieTransferObject movieTransferObject;
    private List<Review> reviews;
    private List<UserShortDto> users;

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

    public List<UserShortDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserShortDto> users) {
        this.users = users;
    }
}
