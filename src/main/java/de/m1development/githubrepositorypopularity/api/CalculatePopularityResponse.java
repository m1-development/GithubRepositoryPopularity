package de.m1development.githubrepositorypopularity.api;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CalculatePopularityResponse {
    private String query;
    private LocalDate earliestDate;
    private String programmingLanguage;
    private Integer matchingRepositoryCount;

    private List<GithubRepositoryItem> repositories;
}
