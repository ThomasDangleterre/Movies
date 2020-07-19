package com.dangleterre.thomas.movies.repositories;

import com.dangleterre.thomas.movies.entities.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

}
