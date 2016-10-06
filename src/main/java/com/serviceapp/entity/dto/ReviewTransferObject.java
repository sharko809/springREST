package com.serviceapp.entity.dto;

import java.sql.Date;

/**
 * Helper class used as DTO for reviews.
 */
public class ReviewTransferObject {

    private String title;

    private Integer rating;

    private String text;

    private Date postDate;

    public ReviewTransferObject() {
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}