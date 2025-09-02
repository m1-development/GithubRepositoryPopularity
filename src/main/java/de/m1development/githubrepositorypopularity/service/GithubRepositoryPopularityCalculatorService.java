package de.m1development.githubrepositorypopularity.service;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;


public interface GithubRepositoryPopularityCalculatorService {

    List<GithubRepositoryItem> calculatePopularityForRepositories(@NonNull String queryString, LocalDate earliestDate, String programmingLanguage);

}
