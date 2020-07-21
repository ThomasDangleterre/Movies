package com.dangleterre.thomas.movies.controllers;


import com.dangleterre.thomas.movies.beans.PostCommentBean;
import com.dangleterre.thomas.movies.entities.CommentEntity;
import com.dangleterre.thomas.movies.services.CommentService;
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
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    CommentService commentService;

    @PostMapping
    @ApiOperation(value = "Insert in database a new comment", notes = "Request body should contain ID of movie already present in database, and commenttext body.Comments should be saved to the application database and returned in request response.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = CommentEntity.class),
            @ApiResponse(code = 404, message = "Not found", response = String.class)
    })
    public ResponseEntity post(@RequestBody PostCommentBean postCommentBean) {
        CommentEntity commentEntity;
        try {
            commentEntity = commentService.insertCommentInDatabase(postCommentBean);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found !");
        }
        return ResponseEntity.ok(commentEntity);
    }

    @GetMapping
    @ApiOperation(value = "Find all or some comments based on movie ID", notes = "Should fetch a list of all comments present in the application database.Should allow filtering comments by associated movie, by passing its ID.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = CommentEntity.class),
            @ApiResponse(code = 404, message = "Not found", response = String.class)
    })
    public ResponseEntity get(@RequestParam(required = false) Long id) {
        List<CommentEntity> commentEntityList;
        try {
            commentEntityList = commentService.findComments(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found !");
        }
        return ResponseEntity.ok(commentEntityList);
    }
}
