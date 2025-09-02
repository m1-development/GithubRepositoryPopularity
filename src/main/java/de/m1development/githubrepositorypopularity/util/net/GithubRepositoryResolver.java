package de.m1development.githubrepositorypopularity.util.net;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

public interface GithubRepositoryResolver {

    /**
     * This method will resolve the matching repositories related to the given query ordered by their stargazers.
     *
     * The result list is limited to the first 500 repositories!
     *
     * @param queryString           Best match query string for repositories on github
     * @param earliestDate          Optional filter to get repositories created on this date
     * @param programmingLanguage   Optional filter to get repositories using the specified programming language
     *
     * @return LIst of GithubRepositoryItem
     */
    List<GithubRepositoryItem> resolveMatchingGithubRepositories(@NonNull String queryString, LocalDate earliestDate, String programmingLanguage);

}
