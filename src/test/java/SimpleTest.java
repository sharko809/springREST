import com.serviceapp.controller.MovieController;
import com.serviceapp.entity.Movie;
import com.serviceapp.entity.util.SortTypeUser;
import com.serviceapp.repository.MovieRepository;
import com.serviceapp.service.MovieService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import resources.TestConfiguration;

import static org.junit.Assert.*;

/**
 * Testing basic functionality
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class SimpleTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testContext() {
        assertTrue(applicationContext.getBeanDefinitionCount() > 0);
    }

    @Test
    public void testGet() {
        assertNotNull(applicationContext.getBean(MovieRepository.class));
    }

    @Test
    public void testGetMovieNotNull() {
        assertNotNull(applicationContext.getBean(MovieRepository.class).findOne(1L));
    }

    @Test
    public void testMovieId() {
        Long movieId = applicationContext.getBean(MovieRepository.class).findOne(1L).getId();
        assertEquals(movieId, new Long(1));
    }

    @Test
    public void testMovieName() {
        String movieName = applicationContext.getBean(MovieRepository.class).findOne(1L).getMovieName();
        assertEquals(movieName, "Warcraft");
    }

    @Test
    public void testMovieService() {
        assertNotNull(applicationContext.getBean(MovieService.class));
    }

    @Test
    public void testGetMovieNonExisting() {
        Movie movie = applicationContext.getBean(MovieService.class).getMovie(9000L);
        assertNull(movie);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParam() {
        applicationContext.getBean(MovieRepository.class).findOne((Long) null);
    }

    @Test
    public void testController() {
        MovieController controller = applicationContext.getBean(MovieController.class);
        assertNotNull(controller.movie(1L));
    }

    @Test
    public void isUserSortType() throws Exception {
        assertTrue(SortTypeUser.isUserSortType("id"));
        assertTrue(SortTypeUser.isUserSortType("login"));
        assertTrue(SortTypeUser.isUserSortType("username"));
        assertTrue(SortTypeUser.isUserSortType("banned"));
        assertTrue(SortTypeUser.isUserSortType("admin"));
        assertFalse(SortTypeUser.isUserSortType("awdawd"));
    }

}
