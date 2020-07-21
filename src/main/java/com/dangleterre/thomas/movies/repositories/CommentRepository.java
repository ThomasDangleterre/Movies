package com.dangleterre.thomas.movies.repositories;

import com.dangleterre.thomas.movies.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("select count(*) from CommentEntity c where c.movieEntity.id=:movieId ")
    long countByMovieId(@Param("movieId") long movieId);
}
