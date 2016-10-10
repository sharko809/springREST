package com.serviceapp.entity.dto;

import javax.validation.constraints.*;

/**
 * Helper class used as DTO for reviews.
 */
public class ReviewTransferObject {

    @NotNull
    @Size(min = 3, max = 100, message = "{review.title.size}")
    @Pattern(regexp = "[a-zA-zа-яА-яё0-9.,]+([ '-][a-zA-Zа-яА-Яё0-9.,]+)*", message = "{review.title.pattern}")
    private String title;

    @NotNull
    @Min(value = 1, message = "{review.rating.min}")
    @Max(value = 10, message = "{review.rating.max}")
    private Integer rating;

    @NotNull
    @Size(min = 5, max = 2000, message = "{review.reviewText.size}")
    @Pattern(regexp = "[a-zA-zа-яА-яё0-9@()!.,+&=?:\\-\"'\\[\\]{\\}]+([ '-][a-zA-Zа-яА-Яё0-9@()!.,+&=?:\\\\\"'\\-\\[\\]{\\}]+)*",
            message = "{review.reviewText.pattern}")
    private String text;

    public ReviewTransferObject() {
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