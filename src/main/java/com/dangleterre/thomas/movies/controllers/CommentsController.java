package com.dangleterre.thomas.movies.controllers;


import com.dangleterre.thomas.movies.beans.PostCommentBean;
import com.dangleterre.thomas.movies.entities.CommentEntity;
import com.dangleterre.thomas.movies.services.CommentService;
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
