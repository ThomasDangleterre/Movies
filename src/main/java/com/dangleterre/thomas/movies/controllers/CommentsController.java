package com.dangleterre.thomas.movies.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @PostMapping
    public String post(){
        return "POST";
    }

    @GetMapping
    public String get(){
        return "GET";
    }
}
