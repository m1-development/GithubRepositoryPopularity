package de.m1development.githubrepositorypopularity.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GithubRepositoryPopularity {

    private int id;
    private String name;
    private String htmlUrl;

    private String language;

    private int forksCount;
    private int stargazersCount;
    private LocalDate updatedAt;

    private int calculatedPopularityScore;
}
