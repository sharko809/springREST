package com.serviceapp.service;

import com.serviceapp.entity.Movie;
import com.serviceapp.repository.MovieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import resources.TestConfiguration;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for <code>MovieService</code> class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class MovieServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void ifMovieExists() throws Exception {
        assertTrue(applicationContext.getBean(MovieService.class).ifMovieExists(3L));
    }

    @Test
    public void countMovies() throws Exception {
        assertTrue(applicationContext.getBean(MovieService.class).countMovies() > 1000);
    }

    @Test
    public void getMovie() throws Exception {
        assertNotNull(applicationContext.getBean(MovieService.class).getMovie(1L));
    }

    @Test
    public void getAllMovies() throws Exception {
        assertNotNull(applicationContext.getBean(MovieService.class).getAllMovies());
        assertTrue(applicationContext.getBean(MovieService.class).getAllMovies().size() > 1000);
    }

    @Test
    public void findMovieByTitle() {
        List<Movie> movies = applicationContext.getBean(MovieService.class).findMovieByTitle("kis");
        assertNotNull(movies);
    }

    @Test
    public void findMovieTopRated() {
        List<Movie> movies = applicationContext.getBean(MovieService.class).findTopRated();
        assertTrue(movies.size() == 10);
    }

}