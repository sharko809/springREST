package com.serviceapp.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.sql.Date;

/**
 * Class representing <code>Review</code> entity.
 */
@Entity
public class Review {

    /**
     * Review id from database
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    /**
     * id of user - author of review
     */
    @NotNull
    private Long userId;

    /**
     * Date when review has been posted
     */
    @NotNull
    private Date postDate;

    /**
     * Title of review
     */
    @NotNull
    @Size(min = 3, max = 100, message = "{review.title.size}")
    @Pattern(regexp = "[\\p{L}\\p{Po}\\p{Mn}\\p{Mc}\\p{Nd}]+([ '-][\\p{L}\\p{Po}\\p{Mn}\\p{Mc}\\p{Nd}]+)*",
            message = "{review.title.pattern}")
    private String title;

    /**
     * id of the movie which review is written to
     */
    @NotNull
    private Long movieId;

    /**
     * Review text written by user
     */
    @NotNull
    @Size(min = 5, max = 2000, message = "{review.reviewText.size}")
    @Pattern(regexp = "[\\p{L}\\p{Po}\\p{Mn}\\p{Mc}\\p{Nd}\\p{Sm}\\p{Ps}\\p{Pe}\\p{Pi}\\p{Pf}]" +
            "+([ '-][\\p{L}\\p{Po}\\p{Mn}\\p{Mc}\\p{Nd}\\p{Sm}\\p{Ps}\\p{Pe}\\p{Pi}\\p{Pf}]+)*",
            message = "{review.reviewText.pattern}")
    private String reviewText;

    /**
     * Rating given by user to movie
     */
    @NotNull
    @Min(value = 1, message = "{review.rating.min}")
    @Max(value = 10, message = "{review.rating.max}")
    private Integer rating;

    public Review() {
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + userId +
                ", postDate=" + postDate +
                ", title='" + title + '\'' +
                ", movieId=" + movieId +
                ", reviewText='" + reviewText + '\'' +
                ", rating=" + rating +
                '}';
    }
}