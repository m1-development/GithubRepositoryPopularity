package de.m1development.githubrepositorypopularity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GithubRepositorySearchResponse {
    @JsonProperty("total_count")
    private Integer totalCount;

    private List<GithubRepositoryItem> items;
}
