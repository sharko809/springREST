package com.serviceapp.entity;

import com.serviceapp.validation.annotation.ValidDate;
import com.serviceapp.validation.annotation.ValidMovieTransferObjectURL;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.sql.Date;

/**
 * Class representing <code>Movie</code> entity.
 */
@Entity
public class Movie {

    /**
     * Movie id from database
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    /**
     * Movie title
     */
    @NotNull
    @Size(min = 1, max = 100, message = "{movie.title.size}")
    @Pattern(regexp = "[a-zA-zа-яА-яё0-9(){},.:'ßé!?üôöóâä-åøí&Åñ]+([ '-][a-zA-Zа-яА-Яё0-9(){},.:'ßé!?üôöóâä-åøí&Åñ]+)*",
            message = "{movie.title.pattern}")
    @Column(name = "moviename")
    private String movieName;

    /**
     * Name of movie's director
     */
    @Size(min = 1, max = 40, message = "{movie.director.size}")
    @Pattern(regexp = "[a-zA-zа-яА-яёöá(){},.:'ßé!?üôóâäåøíÅñ]+([ '-][a-zA-Zа-яА-Яöáё(){},.:'ßé!?üôóâäåøíÅñ]+)*",
            message = "{movie.director.pattern}")
    @Column(name = "director")
    private String director;

    /**
     * Movie release date
     */
    @ValidDate
    @Column(name = "releasedate")
    private Date releaseDate;

    /**
     * URL leading to poster for the movie
     */
    @ValidMovieTransferObjectURL(min = 7, max = 255)
    @Column(name = "posterurl")
    private String posterURL;

    /**
     * URL leading to embed trailer for the movie
     */
    @ValidMovieTransferObjectURL(min = 7, max = 255)
    @Column(name = "trailerurl")
    private String trailerURL;

    /**
     * Movie rating calculated based on users reviews
     */
    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "rating")
    private Double rating;

    /**
     * Some description for the movie
     */
    @NotNull
    @Size(min = 5, max = 2000, message = "{movie.description.size}")
    @Pattern(regexp = "[a-zA-zа-яА-яё0-9@()!.,+&=?:\\-\\\\\"']+([ '-][a-zA-Zа-яА-Яё0-9@()!.,+&=?:\\\\\"'\\-]+)*")
    @Column(name = "description")
    private String description;

    public Movie() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getTrailerURL() {
        return trailerURL;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", movieName='" + movieName + '\'' +
                ", director='" + director + '\'' +
                ", releaseDate=" + releaseDate +
                ", posterURL='" + posterURL + '\'' +
                ", trailerURL='" + trailerURL + '\'' +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                '}';
    }
}