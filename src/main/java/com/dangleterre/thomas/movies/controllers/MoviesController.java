package com.dangleterre.thomas.movies.controllers;

import com.dangleterre.thomas.movies.beans.SearchByTitleBean;
import com.dangleterre.thomas.movies.entities.MovieEntity;
import com.dangleterre.thomas.movies.services.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public ResponseEntity post(@RequestBody SearchByTitleBean search) throws JsonProcessingException {
        if (search == null || search.getTitle() == null || search.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A Title must be set !");
        }
        List<MovieEntity> moviesSaved= movieService.fetchMoviesFromOmdbApi(search.getTitle());
        return ResponseEntity.ok(moviesSaved);
    }

    @GetMapping
    public String get() {
        return movieService.findAllMovies().toString();
    }

    @DeleteMapping("/{id}")
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
