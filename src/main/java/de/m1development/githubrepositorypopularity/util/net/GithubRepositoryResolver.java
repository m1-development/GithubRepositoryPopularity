package de.m1development.githubrepositorypopularity.util.net;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryPopularity;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

public interface GithubRepositoryResolver {

    List<GithubRepositoryPopularity> resolveMatchingGithubRepositories(@NonNull String queryString, LocalDate earliestDate, String programmingLanguage);

}
