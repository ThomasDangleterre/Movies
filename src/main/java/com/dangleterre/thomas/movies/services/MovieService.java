package com.dangleterre.thomas.movies.services;

import com.dangleterre.thomas.movies.beans.MovieBean;
import com.dangleterre.thomas.movies.beans.SearchBean;
import com.dangleterre.thomas.movies.entities.MovieEntity;
import com.dangleterre.thomas.movies.repositories.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    // Api key present in application.yml
    @Value("${external.api-key}")
    private String apiKey;

    RestTemplate restTemplate = new RestTemplate();

    /**
     * Fecth movies from external API and save movies in database
     * @param searchedTitle
     * @return a list a movie entities saved to database
     * @throws JsonProcessingException
     */
    public List<MovieEntity> fetchMoviesFromOmdbApi(String searchedTitle) throws JsonProcessingException {
        // Api call
        final String uri = "http://www.omdbapi.com/?s=\"" + searchedTitle + "\"&apikey="+apiKey;

        String result = restTemplate.getForObject(uri, String.class);

        //Map response to bean
        ObjectMapper mapper = new ObjectMapper();
        SearchBean searchBean = mapper.readValue(result, SearchBean.class);
        //Save to database
        List<MovieEntity> moviesEntitiesSaved = saveMoviesToDatabase(searchBean.getMovies());

        return moviesEntitiesSaved;
    }

    /**
     * Convert a list of movie beans into movie entities and saved them in db
     * @param movieBeanList list of movie beans
     * @return list of movie entities saved to database
     */
    public List<MovieEntity> saveMoviesToDatabase(List<MovieBean> movieBeanList) {
        List<MovieEntity> movieEntitiesSaved = new ArrayList<>();
        //iterate over movies beans
        movieBeanList.stream().forEach(movieBean -> {
            //convert
            MovieEntity movieEntity = convertMovieBeanToEntity(movieBean);
            //Save to db
            movieRepository.saveAndFlush(movieEntity);
            movieEntitiesSaved.add(movieEntity);
        });

        return movieEntitiesSaved;
    }

    /**
     * Convert a movie bean to a movie entity
     * @param movieBean movie bean to convert
     * @return a movie entity
     */
    public MovieEntity convertMovieBeanToEntity(MovieBean movieBean) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setExternalId(movieBean.getExternalId());
        movieEntity.setTitle(movieBean.getTitle());
        movieEntity = setMovieYearFromBean(movieEntity, movieBean);
        return movieEntity;
    }

    /**
     * Set year of begin and end of movie entity
     * handle cases :
     *      -"YYYY-YYYY"
     *      -"YYYY"(begin and end year are the same)
     *      -"YYYY-"(end date is not set, we use the begin date so)
     * @param movieEntity to fill
     * @param movieBean
     * @return
     */
    public MovieEntity setMovieYearFromBean(MovieEntity movieEntity, MovieBean movieBean) {
        // check if movie bean has a string year setted
        if (movieBean.getYear() != null && !movieBean.getYear().isEmpty()) {
            // if year has a "-" in it
            if (movieBean.getYear().contains("–")) {
                // Patter.quote is necessary because "-" is a special character
                // and split expect a regexas parameter
                String yearsArray[] = movieBean.getYear().split(Pattern.quote("–"));
                // if year like "YYYY-YYYY"
                if(yearsArray.length > 1 ) {
                    movieEntity.setYearBegin(Long.parseLong(yearsArray[0]));
                    movieEntity.setYearEnd(Long.parseLong(yearsArray[1]));
                }
                //if year like "YYYY-"
                else {
                    movieEntity.setYearBegin(Long.parseLong(yearsArray[0]));
                    movieEntity.setYearEnd(Long.parseLong(yearsArray[0]));
                }

            }
            // else year is "YYYY"
            else {
                movieEntity.setYearBegin(Long.parseLong(movieBean.getYear()));
                movieEntity.setYearEnd(Long.parseLong(movieBean.getYear()));
            }
        }
        return movieEntity;
    }

    /**
     * Remove an entity by its id
     * @param id movie entity id
     * @return MovieEntity deleted
     * @throws EntityNotFoundException runtime exception
     */
    public MovieEntity removeEntityById(Long id){
        MovieEntity movieEntity = movieRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        movieRepository.delete(movieEntity);
        return movieEntity;
    }

    public List<MovieEntity> findAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * Update a movie title, year or nothing
     * @param id of the movie to update
     * @param title to update or null
     * @param year to update or null
     * @return MovieEntity updated
     * @throws EntityNotFoundException runtime exception
     */
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
