package com.dangleterre.thomas.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dangleterre.thomas.movies.*", "com.dangleterre.thomas.movies"})
@EntityScan(basePackages = "com.dangleterre.thomas.movies.entities")
@EnableJpaRepositories(basePackages = "com.dangleterre.thomas.movies.repositories")
public class MoviesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesApplication.class);
    }

}
