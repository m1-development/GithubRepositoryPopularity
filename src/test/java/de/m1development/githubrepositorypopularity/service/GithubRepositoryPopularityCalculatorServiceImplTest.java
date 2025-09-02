package de.m1development.githubrepositorypopularity.service;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import de.m1development.githubrepositorypopularity.util.net.GithubRepositoryResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.doReturn;

class GithubRepositoryPopularityCalculatorServiceImplTest {

    private final GithubRepositoryResolver githubRepositoryResolver = Mockito.mock(GithubRepositoryResolver.class);

    private GithubRepositoryPopularityCalculatorService sut;

    @BeforeEach
    void setup() {
        sut = new GithubRepositoryPopularityCalculatorServiceImpl(githubRepositoryResolver);
    }

    @Test
    void calculatePopularityForRepositories_requiredParameterOnly() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = null;
        String programmingLanguage = null;

        List<GithubRepositoryItem> testResponse = buildTestResponse();
        doReturn(testResponse).when(githubRepositoryResolver).resolveMatchingGithubRepositories(queryString, earliestDate, programmingLanguage);

        // when
        List<GithubRepositoryItem> list = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void calculatePopularityForRepositories_requiredParameterWithEarliestDate() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = LocalDate.of(2025,9,1);
        String programmingLanguage = null;

        List<GithubRepositoryItem> testResponse = buildTestResponse();
        doReturn(testResponse).when(githubRepositoryResolver).resolveMatchingGithubRepositories(queryString, earliestDate, programmingLanguage);

        // when
        List<GithubRepositoryItem> list = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void calculatePopularityForRepositories_requiredParameterWithProgrammingLanguage() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = null;
        String programmingLanguage = "Java";

        List<GithubRepositoryItem> testResponse = buildTestResponse();
        doReturn(testResponse).when(githubRepositoryResolver).resolveMatchingGithubRepositories(queryString, earliestDate, programmingLanguage);

        // when
        List<GithubRepositoryItem> list = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void calculatePopularityForRepositories_withAllParameters() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = LocalDate.of(2025,9,1);
        String programmingLanguage = "Java";

        List<GithubRepositoryItem> testResponse = buildTestResponse();
        doReturn(testResponse).when(githubRepositoryResolver).resolveMatchingGithubRepositories(queryString, earliestDate, programmingLanguage);

        // when
        List<GithubRepositoryItem> list = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    private List<GithubRepositoryItem> buildTestResponse() {
        GithubRepositoryItem test =  new GithubRepositoryItem();
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