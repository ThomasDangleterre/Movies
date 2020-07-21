package com.dangleterre.thomas.movies.controllers;

import com.dangleterre.thomas.movies.beans.PostCommentBean;
import com.dangleterre.thomas.movies.beans.SearchByTitleBean;
import com.dangleterre.thomas.movies.entities.CommentEntity;
import com.dangleterre.thomas.movies.services.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class CommentsControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockBean
    CommentService commentServiceMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void postTest() throws Exception {
        doReturn(new CommentEntity()).when(commentServiceMock).insertCommentInDatabase(any());
        PostCommentBean postCommentBean = new PostCommentBean();
        String jsonBody = new ObjectMapper().writeValueAsString(postCommentBean);
        mockMvc.perform(post("/comments").contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk());
        verify(commentServiceMock).insertCommentInDatabase(any());

    }

    @Test
    void postNotFoundTest() throws Exception {
        doThrow(new EntityNotFoundException()).when(commentServiceMock).insertCommentInDatabase(any());
        PostCommentBean postCommentBean = new PostCommentBean();
        String jsonBody = new ObjectMapper().writeValueAsString(postCommentBean);
        mockMvc.perform(post("/comments").contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isNotFound());
        verify(commentServiceMock).insertCommentInDatabase(any());
    }

    @Test
    void getTest() throws Exception {
        doReturn(new ArrayList<CommentEntity>()).when(commentServiceMock).findComments(any());
        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk());
        verify(commentServiceMock).findComments(any());
    }

    @Test
    void getNotFoundTest() throws Exception {
        doThrow(new EntityNotFoundException()).when(commentServiceMock).findComments(any());
        mockMvc.perform(get("/comments"))
                .andExpect(status().isNotFound());
        verify(commentServiceMock).findComments(any());
    }
}