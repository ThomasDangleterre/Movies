package com.dangleterre.thomas.movies.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    @PostMapping
    public String post() {
        return "POST";
    }

    @GetMapping
    public String get(){
        return "GET";
    }

    @DeleteMapping("/{id}")
    public String delete(){
        return "DELETE";
    }

    @PutMapping("/{id}")
    public String update(){
        return "UPDATE";
    }
}
