package de.m1development.githubrepositorypopularity.service;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryPopularity;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GithubRepositoryPopularityCalculatorServiceImpl implements GithubRepositoryPopularityCalculatorService {

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

        GithubRepositoryPopularity test =  new GithubRepositoryPopularity();
        test.setId(1234);
        test.setName("RepoName");
        test.setHtmlUrl("https://github.com/RepoName/example");
        test.setLanguage("Java");
        test.setForksCount(50);
        test.setStargazersCount(333);
        test.setUpdatedAt(LocalDate.now());

        test.setCalculatedPopularityScore(200000);

        return List.of(test);
    }
}
