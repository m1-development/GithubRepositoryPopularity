package de.m1development.githubrepositorypopularity.service;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryPopularity;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;


public interface GithubRepositoryPopularityCalculatorService {

    List<GithubRepositoryPopularity> calculatePopularityForRepositories(@NonNull String queryString, LocalDate earliestDate, String programmingLanguage);

}
