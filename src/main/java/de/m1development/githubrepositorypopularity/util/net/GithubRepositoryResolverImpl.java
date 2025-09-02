package de.m1development.githubrepositorypopularity.util.net;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryPopularity;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class GithubRepositoryResolverImpl implements GithubRepositoryResolver {

    @Override
    public List<GithubRepositoryPopularity> resolveMatchingGithubRepositories(
            @NonNull String queryString,
            LocalDate earliestDate,
            String programmingLanguage) {

        // TODO: Implement method to resolve matching repository list from github api
        return List.of();
    }
}
