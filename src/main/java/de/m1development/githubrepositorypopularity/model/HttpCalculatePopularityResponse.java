package de.m1development.githubrepositorypopularity.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class HttpCalculatePopularityResponse {
    private String query;
    private LocalDate earliestDate;
    private String programmingLanguage;
    private Integer matchingRepositoryCount;

    private List<GithubRepositoryItem> repositories;
}
