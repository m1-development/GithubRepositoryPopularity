package de.m1development.githubrepositorypopularity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class HttpCalculatePopularityResponse {
    private String query;

    @JsonProperty("earliest_date")
    private LocalDate earliestDate;

    @JsonProperty("programming_language")
    private String programmingLanguage;

    @JsonProperty("matching_repository_count")
    private Integer matchingRepositoryCount;

    @JsonProperty("matching_repositories")
    private List<GithubRepositoryItem> repositories;
}
