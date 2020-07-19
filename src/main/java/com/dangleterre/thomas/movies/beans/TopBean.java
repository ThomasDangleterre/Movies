package com.dangleterre.thomas.movies.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TopBean {
    @JsonProperty(value = "movie_id")
    private Long movieId;
    @JsonProperty(value = "total_comments")
    private Long totalComments;
    private Long rank;
}
