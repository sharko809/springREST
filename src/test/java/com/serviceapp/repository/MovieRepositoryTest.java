package com.serviceapp.repository;

import com.serviceapp.entity.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import resources.TestConfiguration;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Movie repository test class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class MovieRepositoryTest {

    private static final Long NULL_LONG = null;
    private static final Long NEGATIVE_ID = -1L;
    private static final Long OK_ID = 1L;
    private static final Long ZERO_ID = 0L;
    private static final Long MAX_VALUE = Long.MAX_VALUE;
    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void createMovie() {
        Movie movie = new Movie();
        movie.setMovieName("name");
        movie.setDirector("director");
        movie.setReleaseDate(new java.sql.Date(new java.util.Date().getTime()));
        movie.setPosterURL("url");
        movie.setTrailerURL("url");
        movie.setRating(2d);
        movie.setDescription("description");
        assertNull(movie.getId());
        Movie saved = movieRepository.saveAndFlush(movie);
        assertNotNull(saved.getId());
    }

    @Test
    public void updateExistedMovie() {
        Movie existed = movieRepository.findOne(181L);
        Long oldId = existed.getId();
        String oldName = existed.getMovieName();

        //update name
        existed.setMovieName(existed.getMovieName() + new Random().nextInt());

        Movie updated = movieRepository.saveAndFlush(existed);
        String newName = updated.getMovieName();
        Long newId = updated.getId();
        assertEquals(oldId, newId);
        assertNotEquals(oldName, newName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMovieNull() {
        // must throw IllegalArgumentException if passed param is null
        movieRepository.saveAndFlush(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteMovieExceptionThrow() {
        movieRepository.delete((Movie) null);
    }

    @Test
    public void findOne() throws Exception {
        // must return something with ok id
        assertNotNull(movieRepository.findOne(OK_ID));
        // must return null with id=0
        assertNull(movieRepository.findOne(ZERO_ID));
        // must return null with negative id
        assertNull(movieRepository.findOne(NEGATIVE_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOneExceptionThrow() {
        // must throw IllegalArgumentException with id=null
        assertNull(movieRepository.findOne(NULL_LONG));
    }

    @Test
    public void findAll() throws Exception {
        // must return not null object
        assertNotNull(movieRepository.findAll());
    }

    @Test
    public void count() throws Exception {
        assertTrue(movieRepository.count() > 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void existsExceptionThrow() throws Exception {
        // must throw IllegalArgumentException with id=null
        assertTrue(movieRepository.exists(NULL_LONG));
    }

    @Test
    public void exists() {
        // must return true with ok id
        assertTrue(movieRepository.exists(OK_ID));
        // must return false (no such record)
        assertFalse(movieRepository.exists(MAX_VALUE));
        // must return false for negative id
        assertFalse(movieRepository.exists(NEGATIVE_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByMovieNameContainsAllIgnoreCaseExceptionThrow() throws Exception {
        // must throw IllegalArgumentException if search criteria is null
        assertNotNull(movieRepository.findByMovieNameContainsAllIgnoreCase(null, new PageRequest(0, 10)));
    }

    @Test
    public void findByMovieNameContainsAllIgnoreCase() {
        // must not be null thus "" search criteria results in something like findAll()
        Page<Movie> p = movieRepository.findByMovieNameContainsAllIgnoreCase("", new PageRequest(0, 10));
        assertNotNull(p);
    }

    @Test
    public void findByMovieNameContainsAllIgnoreCaseEmpty() {
        // " " as search criteria finds all titles with spaces
        // alt+255 as search criteria results in empty Page<Movie> object
        Page<Movie> p = movieRepository.findByMovieNameContainsAllIgnoreCase(" ", new PageRequest(0, 10));
        assertNotNull(p);
    }

    @Test
    public void findFirst10ByOrderByRatingDesc() throws Exception {
        // size must be 10 (if there are 10+ records in database)
        assertTrue(movieRepository.findFirst10ByOrderByRatingDesc().size() == 10);
    }

    @Test
    public void findAllPaged() throws Exception {
        // TODO
    }

}