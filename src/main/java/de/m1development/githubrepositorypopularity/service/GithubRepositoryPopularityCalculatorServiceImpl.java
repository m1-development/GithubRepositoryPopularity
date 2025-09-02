package de.m1development.githubrepositorypopularity.service;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryPopularity;
import de.m1development.githubrepositorypopularity.util.net.GithubRepositoryResolver;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GithubRepositoryPopularityCalculatorServiceImpl implements GithubRepositoryPopularityCalculatorService {

    private final GithubRepositoryResolver githubRepositoryResolver;

    @Autowired
    public GithubRepositoryPopularityCalculatorServiceImpl(GithubRepositoryResolver githubRepositoryResolver) {
        this.githubRepositoryResolver = githubRepositoryResolver;
    }

    /**
     * This method will resolve the matching repositories from the github api and calculates
     * a popularity score for each repository based on the stargazers count, the forks count
     * and the recency of updates.
     *
     * @param queryString           Best match query string for repositories on github
     * @param earliestDate          Optional filter to get repositories created on this date
     * @param programmingLanguage   Optional filter to get repositories using the specified programming language
     *
     * @return List of GithubRepositoryPopularity
     */
    @Override
    public List<GithubRepositoryPopularity> calculatePopularityForRepositories(
            @NonNull String queryString,
            LocalDate earliestDate,
            String programmingLanguage) {

        List<GithubRepositoryPopularity> repositories = githubRepositoryResolver.resolveMatchingGithubRepositories(queryString, earliestDate, programmingLanguage);

        // TODO: Implement popularity score algorithm

        return repositories;
    }
}
