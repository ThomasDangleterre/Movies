package com.dangleterre.thomas.movies.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/top")
public class TopController {

    @GetMapping
    public String get(){
        return "GET";
    }
}
