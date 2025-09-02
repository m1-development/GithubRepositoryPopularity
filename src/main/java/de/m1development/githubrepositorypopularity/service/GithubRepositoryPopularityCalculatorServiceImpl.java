package de.m1development.githubrepositorypopularity.service;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
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

    @Override
    public List<GithubRepositoryItem> calculatePopularityForRepositories(
            @NonNull String queryString,
            LocalDate earliestDate,
            String programmingLanguage) {

        List<GithubRepositoryItem> repositories = githubRepositoryResolver.resolveMatchingGithubRepositoriesParallel(
                queryString, earliestDate, programmingLanguage);
        repositories.forEach(GithubRepositoryItem::calculatePopularityScore);
        repositories.sort((a, b) -> Double.compare(b.getPopularityScore(), a.getPopularityScore()));

        return repositories;
    }
}
