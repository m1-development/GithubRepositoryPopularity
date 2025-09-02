package de.m1development.githubrepositorypopularity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GithubRepositoryItem {

    private int id;
    private String name;

    @JsonProperty("html_url")
    private String htmlUrl;

    private String language;

    @JsonProperty("forks_count")
    private int forksCount;

    @JsonProperty("stargazers_count")
    private int stargazersCount;

    @JsonProperty("updated_at")
    private LocalDate updatedAt;

    @JsonIgnore
    private int calculatedPopularityScore = 0;
}
