package com.dangleterre.thomas.movies.services;

import com.dangleterre.thomas.movies.beans.MovieBean;
import com.dangleterre.thomas.movies.entities.MovieEntity;
import com.dangleterre.thomas.movies.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MovieServiceTest {

    @Mock
    RestTemplate restTemplateMock;
    @MockBean
    MovieRepository movieRepository;

    @Spy
    @InjectMocks
    MovieService movieService;

    private MovieBean movieBean;
    private List<MovieBean> movieBeanList;
    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        movieBean = new MovieBean();
        movieBean.setTitle("title");
        movieBean.setExternalId("externalid");
        movieBean.setYear("2010–2015");

        movieBeanList = new ArrayList<>();
        movieBeanList.add(movieBean);
    }

    @Test
    void fetchMoviesFromOmdbApi() throws Exception {
        // Get search.json as string
        ClassLoader classLoader = getClass().getClassLoader();
        String result = Files.readString(Paths.get(classLoader.getResource("search.json").getFile()));

        doReturn(result).when(restTemplateMock).getForObject(anyString(),any());
        doReturn(null).when(movieService).saveMoviesToDatabase(any());

        List<MovieEntity> listMovies = movieService.fetchMoviesFromOmdbApi("test");
        verify(restTemplateMock).getForObject(anyString(),any());
        verify(movieService).saveMoviesToDatabase(any());
    }

    @Test
    void saveMoviesToDatabase() {
        doReturn(null).when(movieRepository).saveAndFlush(any());
        doReturn(new MovieEntity()).when(movieService).convertMovieBeanToEntity(any());
        List<MovieEntity> movieEntityList = movieService.saveMoviesToDatabase(movieBeanList);

        verify(movieRepository).saveAndFlush(any());
        verify(movieService).saveMoviesToDatabase(any());
        assertNotNull(movieEntityList);
        assertFalse(movieEntityList.isEmpty());
    }

    @Test
    void convertMovieBeanToEntity() {
        doReturn(new MovieEntity()).when(movieService).setMovieYearFromBean(any(),any());
        MovieEntity movieEntity = movieService.convertMovieBeanToEntity(movieBean);

        assertEquals("title",movieEntity.getTitle());
        assertEquals("externalid",movieEntity.getExternalId());

        verify(movieService).setMovieYearFromBean(any(),any());
    }

    @Test
    void setMovieYearFromBean() {
        MovieEntity movieEntity = movieService.setMovieYearFromBean(new MovieEntity(),movieBean);
        assertEquals(2010L,movieEntity.getYearBegin());
        assertEquals(2015L,movieEntity.getYearEnd());
    }


    @Test
    void updateMovieEntity() {
        when(movieRepository.findById(any())).thenReturn(Optional.of(new MovieEntity()));
        doReturn(null).when(movieRepository).saveAndFlush(any());
        MovieEntity movieEntity =movieService.updateMovieEntity(1L,"newTitle",null);

        verify(movieRepository).saveAndFlush(any());
        verify(movieRepository).findById(any());

        assertEquals(movieEntity.getTitle(),"newTitle");
        assertNull(movieEntity.getYearBegin());
        assertNull(movieEntity.getYearEnd());

    }
}