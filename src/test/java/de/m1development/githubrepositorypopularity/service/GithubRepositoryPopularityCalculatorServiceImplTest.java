package de.m1development.githubrepositorypopularity.service;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import de.m1development.githubrepositorypopularity.util.net.GithubRepositoryResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.doReturn;

class GithubRepositoryPopularityCalculatorServiceImplTest {

    private final GithubRepositoryResolver githubRepositoryResolver = Mockito.mock(GithubRepositoryResolver.class);

    private GithubRepositoryPopularityCalculatorService sut;

    @BeforeEach
    void setup() {
        sut = new GithubRepositoryPopularityCalculatorServiceImpl(githubRepositoryResolver);
    }

    @Test
    void calculatePopularityForRepositories_withEmptyGithubRepositoryList_ShouldReturnEmptyList() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = LocalDate.now();
        String programmingLanguage = "Java";

        List<GithubRepositoryItem> githubRepositoryItemList = new ArrayList<>();
        doReturn(githubRepositoryItemList).when(githubRepositoryResolver).resolveMatchingGithubRepositoriesParallel(queryString, earliestDate, programmingLanguage);

        // when
        List<GithubRepositoryItem> resultList = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    void calculatePopularityForRepositories_withOneItemGithubRepositoryList_ShouldReturnListWithOneItemAndCalculatedPopularityScore() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = LocalDate.now();
        String programmingLanguage = "Java";

        GithubRepositoryItem item1 =  new GithubRepositoryItem();
        item1.setName("RepoName1");
        item1.setForksCount(20);
        item1.setStargazersCount(333);
        item1.setUpdatedAt(LocalDate.now());

        List<GithubRepositoryItem> githubRepositoryItemList = new ArrayList<>();
        githubRepositoryItemList.add(item1);

        doReturn(githubRepositoryItemList).when(githubRepositoryResolver).resolveMatchingGithubRepositoriesParallel(queryString, earliestDate, programmingLanguage);

        // when
        List<GithubRepositoryItem> resultList = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(1343.0,  resultList.get(0).getPopularityScore());
    }

    @Test
    void calculatePopularityForRepositories_withTwoItemsGithubRepositoryList_ShouldReturnListWithTwoItemsAndCalculatedPopularityScoreOrderedByPopularityScore() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = LocalDate.now();
        String programmingLanguage = "Java";

        GithubRepositoryItem item1 =  new GithubRepositoryItem();
        item1.setName("RepoName1");
        item1.setForksCount(50);
        item1.setStargazersCount(333);
        item1.setUpdatedAt(LocalDate.now().minusDays(30));

        GithubRepositoryItem item2 =  new GithubRepositoryItem();
        item2.setName("RepoName2");
        item2.setForksCount(20);
        item2.setStargazersCount(333);
        item2.setUpdatedAt(LocalDate.now());

        List<GithubRepositoryItem> githubRepositoryItemList = new ArrayList<>();
        githubRepositoryItemList.add(item1);
        githubRepositoryItemList.add(item2);

        doReturn(githubRepositoryItemList).when(githubRepositoryResolver).resolveMatchingGithubRepositoriesParallel(queryString, earliestDate, programmingLanguage);

        // when
        List<GithubRepositoryItem> resultList = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("RepoName2",  resultList.get(0).getName());
        assertEquals(1343.0,  resultList.get(0).getPopularityScore());
        assertEquals("RepoName1",  resultList.get(1).getName());
        assertEquals(691.3333333333333,  resultList.get(1).getPopularityScore());
    }

    @Test
    void calculatePopularityForRepositories_withTwoItemsGithubRepositoryList_moreStargazersCountResultsInHigherPopularityScore() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = LocalDate.now();
        String programmingLanguage = "Java";

        GithubRepositoryItem item1 =  new GithubRepositoryItem();
        item1.setName("RepoName1");
        item1.setForksCount(0);
        item1.setStargazersCount(0);
        item1.setUpdatedAt(LocalDate.now());

        GithubRepositoryItem item2 =  new GithubRepositoryItem();
        item2.setName("RepoName2");
        item2.setForksCount(0);
        item2.setStargazersCount(10);
        item2.setUpdatedAt(LocalDate.now());

        List<GithubRepositoryItem> githubRepositoryItemList = new ArrayList<>();
        githubRepositoryItemList.add(item1);
        githubRepositoryItemList.add(item2);

        doReturn(githubRepositoryItemList).when(githubRepositoryResolver).resolveMatchingGithubRepositoriesParallel(queryString, earliestDate, programmingLanguage);

        // when
        List<GithubRepositoryItem> resultList = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("RepoName2",  resultList.get(0).getName());
        assertEquals(1010.0,  resultList.get(0).getPopularityScore());
        assertEquals("RepoName1",  resultList.get(1).getName());
        assertEquals(1000.0,  resultList.get(1).getPopularityScore());
    }

    @Test
    void calculatePopularityForRepositories_withTwoItemsGithubRepositoryList_moreForksCountResultsInHigherPopularityScore() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = LocalDate.now();
        String programmingLanguage = "Java";

        GithubRepositoryItem item1 =  new GithubRepositoryItem();
        item1.setName("RepoName1");
        item1.setForksCount(0);
        item1.setStargazersCount(0);
        item1.setUpdatedAt(LocalDate.now());

        GithubRepositoryItem item2 =  new GithubRepositoryItem();
        item2.setName("RepoName2");
        item2.setForksCount(20);
        item2.setStargazersCount(0);
        item2.setUpdatedAt(LocalDate.now());

        List<GithubRepositoryItem> githubRepositoryItemList = new ArrayList<>();
        githubRepositoryItemList.add(item1);
        githubRepositoryItemList.add(item2);

        doReturn(githubRepositoryItemList).when(githubRepositoryResolver).resolveMatchingGithubRepositoriesParallel(queryString, earliestDate, programmingLanguage);

        // when
        List<GithubRepositoryItem> resultList = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("RepoName2",  resultList.get(0).getName());
        assertEquals(1010.0,  resultList.get(0).getPopularityScore());
        assertEquals("RepoName1",  resultList.get(1).getName());
        assertEquals(1000.0,  resultList.get(1).getPopularityScore());
    }

    @Test
    void calculatePopularityForRepositories_withTwoItemsGithubRepositoryList_moreRecentUpdatesInHigherPopularityScore() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = LocalDate.now();
        String programmingLanguage = "Java";

        GithubRepositoryItem item1 =  new GithubRepositoryItem();
        item1.setName("RepoName1");
        item1.setForksCount(0);
        item1.setStargazersCount(0);
        item1.setUpdatedAt(LocalDate.now().minusDays(30));

        GithubRepositoryItem item2 =  new GithubRepositoryItem();
        item2.setName("RepoName2");
        item2.setForksCount(0);
        item2.setStargazersCount(0);
        item2.setUpdatedAt(LocalDate.now());

        List<GithubRepositoryItem> githubRepositoryItemList = new ArrayList<>();
        githubRepositoryItemList.add(item1);
        githubRepositoryItemList.add(item2);

        doReturn(githubRepositoryItemList).when(githubRepositoryResolver).resolveMatchingGithubRepositoriesParallel(queryString, earliestDate, programmingLanguage);

        // when
        List<GithubRepositoryItem> resultList = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("RepoName2",  resultList.get(0).getName());
        assertEquals(1000.0,  resultList.get(0).getPopularityScore());
        assertEquals("RepoName1",  resultList.get(1).getName());
        assertEquals(333.3333333333333,  resultList.get(1).getPopularityScore());
    }

    @Test
    void calculatePopularityForRepositories_withMissingQueryString_shouldThrowException() {
        // given
        String queryString = null;
        LocalDate earliestDate = LocalDate.now();
        String programmingLanguage = "Java";

        // when
        // then
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage));

        assertEquals("queryString is marked non-null but is null", exception.getMessage());
    }

}