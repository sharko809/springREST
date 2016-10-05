package com.serviceapp.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    @Column(name = "moviename")
    private String movieName;

    /**
     * Name of movie's director
     */
    @Column(name = "director")
    private String director;

    /**
     * Movie release date
     */
    @Column(name = "releasedate")
    private Date releaseDate;

    /**
     * URL leading to poster for the movie
     */
    @Column(name = "posterurl")
    private String posterURL;

    /**
     * URL leading to embed trailer for the movie
     */
    @Column(name = "trailerurl")
    private String trailerURL;

    /**
     * Movie rating calculated based on users reviews
     */
    @Column(name = "rating")
    private Double rating;

    /**
     * Some description for the movie
     */
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