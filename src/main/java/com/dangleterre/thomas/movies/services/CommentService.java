package com.dangleterre.thomas.movies.services;


import com.dangleterre.thomas.movies.beans.PostCommentBean;
import com.dangleterre.thomas.movies.entities.CommentEntity;
import com.dangleterre.thomas.movies.entities.MovieEntity;
import com.dangleterre.thomas.movies.repositories.CommentRepository;
import com.dangleterre.thomas.movies.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    MovieRepository movieRepository;
    @Autowired
    CommentRepository commentRepository;

    public CommentEntity insertCommentInDatabase(PostCommentBean postCommentBean) {
        MovieEntity movieEntity = movieRepository.findById(postCommentBean.getIdMovie()).orElseThrow(EntityNotFoundException::new);
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setMovieEntity(movieEntity);
        commentEntity.setCommentText(postCommentBean.getCommentText());
        commentRepository.saveAndFlush(commentEntity);
        return commentEntity;
    }

    public List<CommentEntity> findComments(Long id) {
        List<CommentEntity> commentRepositoryList = commentRepository.findAll();
        if (id != null) {
            MovieEntity movieEntity = movieRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            commentRepositoryList = commentRepositoryList.stream()
                    .filter(comment -> comment.getMovieEntity().getId().equals(movieEntity.getId()))
                    .collect(Collectors.toList());
        }
        return commentRepositoryList;
    }
}
