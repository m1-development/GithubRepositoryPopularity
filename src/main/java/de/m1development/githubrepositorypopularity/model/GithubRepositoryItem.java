package de.m1development.githubrepositorypopularity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
    private Double popularityScore = 0.0;

    @JsonIgnore
    public void calculatePopularityScore() {
        double stargazersCountFactor = 1.0;
        double forksCountFactor = 0.5;
        double updateFactor = 1000.0;
        double updateWeightDivisor = 15.0;

        double daysSinceLastUpdate = ChronoUnit.DAYS.between(LocalDate.now(), updatedAt);
        double daysSinceLastUpdateWeight = daysSinceLastUpdate * -1.0 / updateWeightDivisor;
        double updateScore = updateFactor * (1.0 / (1.0 + daysSinceLastUpdateWeight));

        this.popularityScore = stargazersCount * stargazersCountFactor + forksCount * forksCountFactor + updateScore;
    }
}
