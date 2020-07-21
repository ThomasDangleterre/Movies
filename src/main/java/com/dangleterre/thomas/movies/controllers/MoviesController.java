package com.dangleterre.thomas.movies.controllers;

import com.dangleterre.thomas.movies.beans.SearchByTitleBean;
import com.dangleterre.thomas.movies.entities.CommentEntity;
import com.dangleterre.thomas.movies.entities.MovieEntity;
import com.dangleterre.thomas.movies.services.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MoviesController {
    @Autowired
    MovieService movieService;

    @PostMapping
    @ApiOperation(value = "Fetch movies data from external api to database", notes = "Request body should contain only movie title, and its presence should be validated. Based on passed title, other movie details should be fetched from http://www.omdbapi.com ​ (or other similar, public movie database) and saved to the application database.Request response should include full movie object, along with all data fetched from external API.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = List.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class)
    })
    public ResponseEntity post(@RequestBody SearchByTitleBean search) throws JsonProcessingException {
        if (search == null || search.getTitle() == null || search.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A Title must be set !");
        }
        List<MovieEntity> moviesSaved= movieService.fetchMoviesFromOmdbApi(search.getTitle());
        return ResponseEntity.ok(moviesSaved);
    }

    @GetMapping
    @ApiOperation(value = "List of all movies in database", notes = "Get the movie if it exists and then delete it and returning a response")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = List.class),
    })
    public String get() {
        return movieService.findAllMovies().toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a movie", notes = "Get the movie if it exists and then delete it and returning a response.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 404, message = "Not found", response = String.class)
    })
    public ResponseEntity delete(@PathVariable Long id) {
        MovieEntity movieEntity;
        try {
            movieEntity = movieService.removeEntityById(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found !");
        }
        return ResponseEntity.ok(movieEntity.getTitle() + " has been deleted");
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a movie", notes = "Update the movie by sending a PUT request to the URL with the data you want to update.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 404, message = "Not found", response = String.class)
    })
    public ResponseEntity update(@PathVariable Long id, @RequestParam(required = false) String title, @RequestParam(required = false) String year) {
        MovieEntity updatedMovieEntity;
        try {
            updatedMovieEntity = movieService.updateMovieEntity(id, title, year);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found !");
        }
        return ResponseEntity.ok().body(updatedMovieEntity);
    }
}
