package com.serviceapp.service;

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

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for <code>MovieService</code> class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class MovieServiceTest {

    private static final Long NULL_LONG = null;
    private static final Long NEGATIVE_ID = -1L;
    private static final Long OK_ID = 1L;
    private static final Long ZERO_ID = 0L;
    private static final PageRequest PAGE_REQUEST = new PageRequest(0, 10);

    @Autowired
    private MovieService movieService;

    @Test
    public void createMovie() {
        assertNull(movieService.createMovie(null));
    }

    @Test
    public void updateMovie() {
        assertNull(movieService.updateMovie(null));
    }

    @Test
    public void ifMovieExists() throws Exception {
        assertTrue(movieService.ifMovieExists(OK_ID));
        assertFalse(movieService.ifMovieExists(NEGATIVE_ID));
        assertFalse(movieService.ifMovieExists(ZERO_ID));
    }

    @Test
    public void ifMovieExistsNullClause() throws Exception {
        // null value treated in a way that results in false
        assertFalse(movieService.ifMovieExists(NULL_LONG));
    }

    @Test
    public void countMovies() throws Exception {
        assertTrue(movieService.countMovies() > 1000);
    }

    @Test
    public void getMovie() throws Exception {
        assertNotNull(movieService.getMovie(OK_ID));
        // must return null
        assertNull(movieService.getMovie(NULL_LONG));
        assertNull(movieService.getMovie(NEGATIVE_ID));
        assertNull(movieService.getMovie(ZERO_ID));
    }

    @Test
    public void getAllMovies() throws Exception {
        assertNotNull(movieService.getAllMovies());
        assertTrue(movieService.getAllMovies().size() > 1000);
    }

    @Test
    public void findMovieByTitle() {
        Page<Movie> movies = movieService.findMovieByTitle("kis", PAGE_REQUEST);
        assertNotNull(movies);
    }

    @Test
    public void findMovieByTitleNullEmpty() {
        assertNull(movieService.findMovieByTitle(null, PAGE_REQUEST));
        assertNull(movieService.findMovieByTitle("", PAGE_REQUEST));
        assertNull(movieService.findMovieByTitle(" ", PAGE_REQUEST));
        assertNull(movieService.findMovieByTitle(" ", PAGE_REQUEST));
    }

    @Test
    public void findMovieTopRated() {
        List<Movie> movies = movieService.findTopRated();
        assertTrue(movies.size() == 10);
    }

    @Test
    public void findAllPaged() {
        // TODO
    }

}