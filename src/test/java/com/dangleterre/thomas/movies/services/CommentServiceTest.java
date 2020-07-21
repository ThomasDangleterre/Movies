package com.dangleterre.thomas.movies.services;

import com.dangleterre.thomas.movies.beans.PostCommentBean;
import com.dangleterre.thomas.movies.entities.CommentEntity;
import com.dangleterre.thomas.movies.entities.MovieEntity;
import com.dangleterre.thomas.movies.repositories.CommentRepository;
import com.dangleterre.thomas.movies.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class CommentServiceTest {

    @MockBean
    CommentRepository commentRepositoryMock;
    @MockBean
    MovieRepository movieRepositoryMock;

    @InjectMocks
    CommentService commentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void insertCommentInDatabase() throws Exception {
        when(movieRepositoryMock.findById(any())).thenReturn(Optional.of(new MovieEntity()));
        doReturn(null).when(commentRepositoryMock).saveAndFlush(any());

        PostCommentBean postCommentBean =new PostCommentBean();
        postCommentBean.setCommentText("text");

        CommentEntity commentEntity = commentService.insertCommentInDatabase(postCommentBean);

        verify(commentRepositoryMock).saveAndFlush(any());
        assertEquals("text",commentEntity.getCommentText());
    }

}