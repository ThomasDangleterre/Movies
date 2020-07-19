package com.dangleterre.thomas.movies.services;

import com.dangleterre.thomas.movies.beans.MovieBean;
import com.dangleterre.thomas.movies.beans.SearchBean;
import com.dangleterre.thomas.movies.entities.MovieEntity;
import com.dangleterre.thomas.movies.repositories.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public String fetchMoviesFromOmdbApi(String searchedTitle) throws JsonProcessingException {
        //@TODO add constante for api key
        final String uri = "http://www.omdbapi.com/?s=\"" + searchedTitle + "\"&apikey=f707c09";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        ObjectMapper mapper = new ObjectMapper();
        SearchBean searchBean = mapper.readValue(result, SearchBean.class);
        List<MovieEntity> moviesEntitiesSaved = saveMoviesToDatabase(searchBean.getMovies());

        return moviesEntitiesSaved.toString();
    }

    public List<MovieEntity> saveMoviesToDatabase(List<MovieBean> movieBeanList) {
        List<MovieEntity> movieEntitiesSaved = new ArrayList<>();
        movieBeanList.stream().forEach(movieBean -> {
            MovieEntity movieEntity = convertMovieBeanToEntity(movieBean);
            movieRepository.saveAndFlush(movieEntity);
            movieEntitiesSaved.add(movieEntity);
        });
        return movieEntitiesSaved;
    }

    public MovieEntity convertMovieBeanToEntity(MovieBean movieBean) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setExternalId(movieBean.getExternalId());
        movieEntity.setTitle(movieBean.getTitle());
        movieEntity = setMovieYearFromBean(movieEntity, movieBean);
        return movieEntity;
    }

    public MovieEntity setMovieYearFromBean(MovieEntity movieEntity, MovieBean movieBean) {
        if (movieBean.getYear() != null) {
            if (movieBean.getYear().contains("–")) {
                String yearsArray[] = movieBean.getYear().split(Pattern.quote("–"));
                movieEntity.setYearBegin(Long.parseLong(yearsArray[0]));
                movieEntity.setYearEnd(Long.parseLong(yearsArray[1]));
            } else {
                movieEntity.setYearBegin(Long.parseLong(movieBean.getYear()));
                movieEntity.setYearEnd(Long.parseLong(movieBean.getYear()));
            }
        }
        return movieEntity;
    }

    public MovieEntity removeEntityById(Long id){
        MovieEntity movieEntity = movieRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        movieRepository.delete(movieEntity);
        return movieEntity;
    }

    public List<MovieEntity> findAllMovies() {
        return movieRepository.findAll();
    }

    public MovieEntity updateMovieEntity(Long id, String title, String year)  {
        MovieEntity movieEntity = movieRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (title != null) {
            movieEntity.setTitle(title);
        }
        if (year != null) {
            MovieBean movieBean = new MovieBean();
            movieBean.setYear(year);
            movieEntity = setMovieYearFromBean(movieEntity, movieBean);
        }
        movieRepository.saveAndFlush(movieEntity);
        return movieEntity;
    }
}
