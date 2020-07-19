package com.dangleterre.thomas.movies.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class MovieEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String externalId;
    private Long yearBegin;
    private Long yearEnd;

}
