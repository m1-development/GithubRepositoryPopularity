package de.m1development.githubrepositorypopularity.util.net;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

public interface GithubRepositoryResolver {

    List<GithubRepositoryItem> resolveMatchingGithubRepositories(@NonNull String queryString, LocalDate earliestDate, String programmingLanguage);

}
