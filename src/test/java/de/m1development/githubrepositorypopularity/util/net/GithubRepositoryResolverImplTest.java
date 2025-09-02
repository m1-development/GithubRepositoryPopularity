package de.m1development.githubrepositorypopularity.util.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import de.m1development.githubrepositorypopularity.model.GithubRepositorySearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GithubRepositoryResolverImplTest {

    private GithubRepositoryResolverImpl sut;

    @BeforeEach
    void setUp() {
        sut = Mockito.spy(new GithubRepositoryResolverImpl());
    }

    @Test
    void resolveMatchingGithubRepositoriesParallel_withEmptyGithubRepositoryResponse_ShouldReturnAnEmptyListSequential() throws IOException, InterruptedException {
        // given
        String queryString = "test";
        LocalDate earliestDate = LocalDate.of(2024,1,1);
        String programmingLanguage = "python";

        GithubRepositorySearchResponse responsePage1 = new GithubRepositorySearchResponse();
        responsePage1.setTotalCount(0);
        responsePage1.setItems(new ArrayList<>());

        doReturn(responsePage1).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);

        // when
        List<GithubRepositoryItem> response = sut.resolveMatchingGithubRepositoriesParallel(queryString, earliestDate, programmingLanguage);

        // then
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);
        verify(sut, never()).resolveMissingRepositoriesInParallel(queryString, earliestDate, programmingLanguage, 1);
        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void resolveMatchingGithubRepositoriesParallel_withMoreThan100Results_ShouldReturnConcatenatedResultsFrom2Pages() throws IOException, InterruptedException {
        // given
        String queryString = "test";
        LocalDate earliestDate = LocalDate.of(2024,1,1);
        String programmingLanguage = "python";

        int totalResultCount = 150;
        GithubRepositorySearchResponse responsePage1 = buildPageResponse("namePage1", totalResultCount);
        GithubRepositorySearchResponse responsePage2 = buildPageResponse("namePage2", totalResultCount);

        doReturn(responsePage1).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);

        List<GithubRepositoryItem> resultList = new ArrayList<>(responsePage2.getItems());
        doReturn(resultList).when(sut).resolveMissingRepositoriesInParallel(queryString, earliestDate, programmingLanguage, 2);

        // when
        List<GithubRepositoryItem> response = sut.resolveMatchingGithubRepositoriesParallel(queryString, earliestDate, programmingLanguage);

        // then
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);
        verify(sut).resolveMissingRepositoriesInParallel(queryString, earliestDate, programmingLanguage, 2);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("namePage1", response.get(0).getName());
        assertEquals("namePage2", response.get(1).getName());
    }

    @Test
    void resolveMatchingGithubRepositoriesParallel_withMoreThan500Results_ShouldReturnConcatenatedResultsLimitedTo5Pages() throws IOException, InterruptedException {
        // given
        String queryString = "test";
        LocalDate earliestDate = LocalDate.of(2024,1,1);
        String programmingLanguage = "python";

        int totalResultCount = 765;
        GithubRepositorySearchResponse responsePage1 = buildPageResponse("namePage1", totalResultCount);
        GithubRepositorySearchResponse responsePage2 = buildPageResponse("namePage2", totalResultCount);
        GithubRepositorySearchResponse responsePage3 = buildPageResponse("namePage3", totalResultCount);
        GithubRepositorySearchResponse responsePage4 = buildPageResponse("namePage4", totalResultCount);
        GithubRepositorySearchResponse responsePage5 = buildPageResponse("namePage5", totalResultCount);

        doReturn(responsePage1).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);

        List<GithubRepositoryItem> resultList = new ArrayList<>();
        resultList.addAll(responsePage2.getItems());
        resultList.addAll(responsePage3.getItems());
        resultList.addAll(responsePage4.getItems());
        resultList.addAll(responsePage5.getItems());
        doReturn(resultList).when(sut).resolveMissingRepositoriesInParallel(queryString, earliestDate, programmingLanguage, 5);

        // when
        List<GithubRepositoryItem> response = sut.resolveMatchingGithubRepositoriesParallel(queryString, earliestDate, programmingLanguage);

        // then
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);
        verify(sut).resolveMissingRepositoriesInParallel(queryString, earliestDate, programmingLanguage, 5);
        assertNotNull(response);
        assertEquals(5, response.size());
        assertEquals("namePage1", response.get(0).getName());
        assertEquals("namePage2", response.get(1).getName());
        assertEquals("namePage3", response.get(2).getName());
        assertEquals("namePage4", response.get(3).getName());
        assertEquals("namePage5", response.get(4).getName());
    }


    @Test
    void resolveMatchingGithubRepositoriesSequential_withEmptyGithubRepositoryResponse_ShouldReturnAnEmptyListSequential() throws IOException, InterruptedException {
        // given
        String queryString = "test";
        LocalDate earliestDate = LocalDate.of(2024,1,1);
        String programmingLanguage = "python";

        GithubRepositorySearchResponse responsePage1 = new GithubRepositorySearchResponse();
        responsePage1.setTotalCount(0);
        responsePage1.setItems(new ArrayList<>());

        doReturn(responsePage1).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);

        // when
        List<GithubRepositoryItem> response = sut.resolveMatchingGithubRepositoriesSequential(queryString, earliestDate, programmingLanguage);

        // then
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);
        verify(sut, never()).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 2);
        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void resolveMatchingGithubRepositoriesSequential_withMoreThan100Results_ShouldReturnConcatenatedResultsFrom2Pages() throws IOException, InterruptedException {
        // given
        String queryString = "test";
        LocalDate earliestDate = LocalDate.of(2024,1,1);
        String programmingLanguage = "python";

        int totalResultCount = 150;
        GithubRepositorySearchResponse responsePage1 = buildPageResponse("namePage1", totalResultCount);
        GithubRepositorySearchResponse responsePage2 = buildPageResponse("namePage2", totalResultCount);

        doReturn(responsePage1).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);
        doReturn(responsePage2).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 2);

        // when
        List<GithubRepositoryItem> response = sut.resolveMatchingGithubRepositoriesSequential(queryString, earliestDate, programmingLanguage);

        // then
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 2);
        verify(sut, never()).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 3);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("namePage1", response.get(0).getName());
        assertEquals("namePage2", response.get(1).getName());
    }

    @Test
    void resolveMatchingGithubRepositoriesSequential_withMoreThan500Results_ShouldReturnConcatenatedResultsLimitedTo5Pages() throws IOException, InterruptedException {
        // given
        String queryString = "test";
        LocalDate earliestDate = LocalDate.of(2024,1,1);
        String programmingLanguage = "python";

        int totalResultCount = 765;
        GithubRepositorySearchResponse responsePage1 = buildPageResponse("namePage1", totalResultCount);
        GithubRepositorySearchResponse responsePage2 = buildPageResponse("namePage2", totalResultCount);
        GithubRepositorySearchResponse responsePage3 = buildPageResponse("namePage3", totalResultCount);
        GithubRepositorySearchResponse responsePage4 = buildPageResponse("namePage4", totalResultCount);
        GithubRepositorySearchResponse responsePage5 = buildPageResponse("namePage5", totalResultCount);

        doReturn(responsePage1).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);
        doReturn(responsePage2).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 2);
        doReturn(responsePage3).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 3);
        doReturn(responsePage4).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 4);
        doReturn(responsePage5).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 5);

        // when
        List<GithubRepositoryItem> response = sut.resolveMatchingGithubRepositoriesSequential(queryString, earliestDate, programmingLanguage);

        // then
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 2);
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 3);
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 4);
        verify(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 5);
        verify(sut, never()).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 6);
        assertNotNull(response);
        assertEquals(5, response.size());
        assertEquals("namePage1", response.get(0).getName());
        assertEquals("namePage2", response.get(1).getName());
        assertEquals("namePage3", response.get(2).getName());
        assertEquals("namePage4", response.get(3).getName());
        assertEquals("namePage5", response.get(4).getName());
    }

    private GithubRepositorySearchResponse buildPageResponse(String pageName, int totalCount) {
        GithubRepositorySearchResponse responsePage1 = new GithubRepositorySearchResponse();
        responsePage1.setTotalCount(totalCount);
        responsePage1.setItems(buildTestItems(pageName));
        return responsePage1;
    }

    private List<GithubRepositoryItem> buildTestItems(String name) {
        GithubRepositoryItem item = new GithubRepositoryItem();
        item.setName(name);

        List<GithubRepositoryItem> items = new ArrayList<>();
        items.add(item);

        return items;
    }

    @Test
    void buildHttpRequest_withCommonRequestValues_ShuldReturnAValidHttpRequest() {
        // given
        String queryString = "test";
        LocalDate earliestDate = null;
        String programmingLanguage = null;
        Integer page = 1;

        // when
        HttpRequest httpRequest = sut.buildHttpRequest(queryString, earliestDate, programmingLanguage, page);

        // then
        assertNotNull(httpRequest);

        Map<String, List<String>> headersMap = httpRequest.headers().map();
        assertNotNull(headersMap);
        assertEquals(1, headersMap.size());
        assertTrue(headersMap.containsKey("Accept") && headersMap.get("Accept").contains("application/vnd.github.v3+json"));

        URI uri = httpRequest.uri();
        assertNotNull(uri);
        assertEquals("https", uri.getScheme());
        assertEquals("api.github.com", uri.getHost());
        assertEquals("/search/repositories", uri.getPath());
    }

    @Test
    void buildHttpRequest_OnlyWithQueryString_ShouldCreateAValidGithubURIWithinHttpRequest() {
        // given
        String queryString = "test";
        LocalDate earliestDate = null;
        String programmingLanguage = null;
        Integer page = 1;

        // when
        HttpRequest httpRequest = sut.buildHttpRequest(queryString, earliestDate, programmingLanguage, page);

        // then
        assertNotNull(httpRequest);
        URI uri = httpRequest.uri();
        assertNotNull(uri);
        assertEquals("q=test&page=1&per_page=100&sort=stars&order=desc", uri.getQuery());
    }

    @Test
    void buildHttpRequest_WithQueryStringAndEarliestDate_ShouldCreateAValidGithubURIWithinHttpRequest() {
        // given
        String queryString = "test";
        LocalDate earliestDate = LocalDate.of(2024,1,1);
        String programmingLanguage = null;
        Integer page = 1;

        // when
        HttpRequest httpRequest = sut.buildHttpRequest(queryString, earliestDate, programmingLanguage, page);

        // then
        assertNotNull(httpRequest);
        URI uri = httpRequest.uri();
        assertNotNull(uri);
        assertEquals("q=test+created:>=2024-01-01&page=1&per_page=100&sort=stars&order=desc", uri.getQuery());
    }

    @Test
    void buildHttpRequest_WithQueryStringAndProgrammingLanguage_ShouldCreateAValidGithubURIWithinHttpRequest() {
        // given
        String queryString = "test";
        LocalDate earliestDate = null;
        String programmingLanguage = "java";
        Integer page = 1;

        // when
        HttpRequest httpRequest = sut.buildHttpRequest(queryString, earliestDate, programmingLanguage, page);

        // then
        assertNotNull(httpRequest);
        URI uri = httpRequest.uri();
        assertNotNull(uri);
        assertEquals("q=test+language:java&page=1&per_page=100&sort=stars&order=desc", uri.getQuery());
    }

    @Test
    void buildHttpRequest_WithQueryStringAndEarliestDateAndProgrammingLanguage_ShouldCreateAValidGithubURIWithinHttpRequest() {
        // given
        String queryString = "test";
        LocalDate earliestDate = LocalDate.of(2024,1,1);
        String programmingLanguage = "java";
        Integer page = 1;

        // when
        HttpRequest httpRequest = sut.buildHttpRequest(queryString, earliestDate, programmingLanguage, page);

        // then
        assertNotNull(httpRequest);
        URI uri = httpRequest.uri();
        assertNotNull(uri);
        assertEquals("q=test+created:>=2024-01-01+language:java&page=1&per_page=100&sort=stars&order=desc", uri.getQuery());
    }

    @Test
    void buildHttpRequest_MissingQueryString_ShouldThrowException() {
        // given
        String queryString = null;
        LocalDate earliestDate = null;
        String programmingLanguage = null;
        Integer page = 1;

        // when
        // then
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> sut.buildHttpRequest(queryString, earliestDate, programmingLanguage, page));

        assertEquals("queryString is marked non-null but is null", exception.getMessage());
    }

    @Test
    void parseJsonResponse_WithEmptyJsonResponse_ShouldFillGithubRepositorySearchResponseCorrectly() throws JsonProcessingException {
        // given
        String jsonResponse = "{\"total_count\": 0, \"items\": []}";

        // when
        GithubRepositorySearchResponse parsedJsonResponse = sut.parseJsonResponse(jsonResponse);

        // then
        assertNotNull(parsedJsonResponse);
        assertEquals(0, parsedJsonResponse.getTotalCount());
        assertEquals(0, parsedJsonResponse.getItems().size());
    }

    @Test
    void parseJsonResponse_WithOneItemResponse_ShouldFillGithubRepositorySearchResponseCorrectly() throws JsonProcessingException {
        // given
        String jsonResponse = "{" +
                "\"total_count\": 1, " +
                "\"items\": [" +
                "{" +
                "\"id\":1234, " +
                "\"name\":\"RepoName\", " +
                "\"html_url\":\"https://github.com/OwnerName/RepoName\", " +
                "\"language\":\"Java\", " +
                "\"forks_count\":12, " +
                "\"stargazers_count\":5, " +
                "\"updated_at\":\"2025-09-01\", " +
                "\"ignore_unknown_value\":\"uninteresting value\"" +
                "}" +
                "]" +
                "}";

        // when
        GithubRepositorySearchResponse parsedJsonResponse = sut.parseJsonResponse(jsonResponse);

        // then
        assertNotNull(parsedJsonResponse);
        assertEquals(1, parsedJsonResponse.getTotalCount());
        assertEquals(1, parsedJsonResponse.getItems().size());

        GithubRepositoryItem item = parsedJsonResponse.getItems().get(0);
        assertNotNull(item);
        assertEquals(1234, item.getId());
        assertEquals("RepoName", item.getName());
        assertEquals("https://github.com/OwnerName/RepoName", item.getHtmlUrl());
        assertEquals("Java", item.getLanguage());
        assertEquals(12, item.getForksCount());
        assertEquals(5, item.getStargazersCount());
        assertEquals(LocalDate.of(2025, 9, 1), item.getUpdatedAt());
    }
}