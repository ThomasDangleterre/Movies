package com.dangleterre.thomas.movies.controllers;

import com.dangleterre.thomas.movies.beans.SearchByTitleBean;
import com.dangleterre.thomas.movies.entities.MovieEntity;
import com.dangleterre.thomas.movies.services.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class MoviesControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieServiceMock;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void postTest() throws Exception {
        doReturn("OK").when(movieServiceMock).fetchMoviesFromOmdbApi(anyString());
        SearchByTitleBean searchByTitleBean = new SearchByTitleBean();
        searchByTitleBean.setTitle("hello");
        String jsonBody = new ObjectMapper().writeValueAsString(searchByTitleBean);
        mockMvc.perform(post("/movies").contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    void postNullTitle() throws Exception{
        SearchByTitleBean searchByTitleBean = new SearchByTitleBean();

        String jsonBody = new ObjectMapper().writeValueAsString(searchByTitleBean);
        mockMvc.perform(post("/movies").contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTest() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTest() throws Exception {
        doReturn(new MovieEntity()).when(movieServiceMock).removeEntityById(any());
        mockMvc.perform(delete("/movies/1")).andExpect(status().isOk());
        verify(movieServiceMock).removeEntityById(any());
    }

    @Test
    void deleteNotFoundTest() throws Exception {
        doThrow(new EntityNotFoundException()).when(movieServiceMock).removeEntityById(any());
        mockMvc.perform(delete("/movies/1"))
                .andExpect(status().isNotFound());
        verify(movieServiceMock).removeEntityById(any());
    }

    @Test
    void updateTest() throws Exception {
        doReturn(new MovieEntity()).when(movieServiceMock).updateMovieEntity(any(),any(),any());
        mockMvc.perform(put("/movies/1"))
                .andExpect(status().isOk());
        verify(movieServiceMock).updateMovieEntity(any(),any(),any());
    }

    @Test
    void updateNotFoundTest() throws Exception {
        doThrow(new EntityNotFoundException()).when(movieServiceMock).updateMovieEntity(any(),any(),any());
        mockMvc.perform(put("/movies/1"))
                .andExpect(status().isNotFound());
        verify(movieServiceMock).updateMovieEntity(any(),any(),any());
    }

}