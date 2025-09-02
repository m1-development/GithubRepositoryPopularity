package de.m1development.githubrepositorypopularity.service;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;


public interface GithubRepositoryPopularityCalculatorService {

    /**
     * This method will calculate a popularity score for each resolved repository based on the stargazers count,
     * the forks count and the recency of updates.
     *
     * @param queryString           Best match query string for repositories on github
     * @param earliestDate          Optional filter to get repositories created on this date
     * @param programmingLanguage   Optional filter to get repositories using the specified programming language
     *
     * @return List of GithubRepositoryItem with calculated popularity score
     */
    List<GithubRepositoryItem> calculatePopularityForRepositories(@NonNull String queryString, LocalDate earliestDate, String programmingLanguage);

}
