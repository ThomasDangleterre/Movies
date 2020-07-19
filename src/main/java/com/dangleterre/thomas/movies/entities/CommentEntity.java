package com.dangleterre.thomas.movies.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CommentEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private MovieEntity movieEntity;

    private String commentText;
}
