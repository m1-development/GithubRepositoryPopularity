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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class GithubRepositoryResolverImplTest {

    private GithubRepositoryResolverImpl sut;

    @BeforeEach
    void setUp() {
        sut = Mockito.spy(new GithubRepositoryResolverImpl());
    }

    @Test
    void resolveMatchingGithubRepositories() throws IOException, InterruptedException {
        // given
        String queryString = "test";
        LocalDate earliestDate = LocalDate.of(2024,1,1);
        String programmingLanguage = "python";

        GithubRepositorySearchResponse testResponse = buildTestResponse();
        doReturn(testResponse).when(sut).sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, 1);

        // when
        List<GithubRepositoryItem> response = sut.resolveMatchingGithubRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(response);
        assertEquals(0, response.size());
    }

    private GithubRepositorySearchResponse buildTestResponse() {

        GithubRepositorySearchResponse response = new GithubRepositorySearchResponse();
        response.setTotalCount(0);
        response.setItems(List.of());

        return response;
    }

    @Test
    void buildHttpRequest_commonRequestValues() {
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
    void buildHttpRequest_URIonlyWithQueryString() {
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
    void buildHttpRequest_URIWithQueryStringAndEarliestDate() {
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
    void buildHttpRequest_URIWithQueryStringAndProgrammingLanguage() {
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
    void buildHttpRequest_URIWithQueryStringAndEarliestDateAndProgrammingLanguage() {
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
    void buildHttpRequest_MissingQuerySring() {
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
    void parseJsonResponse_EmptyResponse() throws JsonProcessingException {
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
    void parseJsonResponse_OneItem() throws JsonProcessingException {
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