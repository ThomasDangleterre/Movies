package com.dangleterre.thomas.movies.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieBean {

    @JsonProperty(value = "Title")
    private String title;
    @JsonProperty(value = "imdbID")
    private String externalId;
    @JsonProperty(value = "Year")
    private String year;
}
