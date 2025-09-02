package de.m1development.githubrepositorypopularity.util.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import de.m1development.githubrepositorypopularity.model.GithubRepositorySearchResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.net.http.HttpResponse.BodyHandlers.ofString;

@Component
public class GithubRepositoryResolverImpl implements GithubRepositoryResolver {

    public static final String GITHUB_HOST = "api.github.com";
    public static final int PAGE_SIZE = 100;
    public static final int RESULT_LIMIT = 500;

    @Override
    public List<GithubRepositoryItem> resolveMatchingGithubRepositories(
            @NonNull String queryString,
            LocalDate earliestDate,
            String programmingLanguage) {

        List<GithubRepositoryItem> collectedGithubRepositoryItems = new ArrayList<>();
        int currentPage = 1;
        boolean searchIsComplete;

        try {
            do {
                GithubRepositorySearchResponse githubRepositorySearchResponse =
                        sendHttpRequestAndParseResponse(queryString, earliestDate, programmingLanguage, currentPage);

                collectedGithubRepositoryItems.addAll(githubRepositorySearchResponse.getItems());

                int totalSearchCount = githubRepositorySearchResponse.getTotalCount() > RESULT_LIMIT ?
                        RESULT_LIMIT : githubRepositorySearchResponse.getTotalCount();

                searchIsComplete = totalSearchCount <= currentPage * PAGE_SIZE;
                currentPage++;

            } while (!searchIsComplete);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return collectedGithubRepositoryItems;
    }

    GithubRepositorySearchResponse sendHttpRequestAndParseResponse(
            String queryString,
            LocalDate earliestDate,
            String programmingLanguage,
            Integer page) throws IOException, InterruptedException {

        HttpRequest request = buildHttpRequest(queryString, earliestDate, programmingLanguage, page);

        String jsonResponse = sendHttpRequest(request);

        return parseJsonResponse(jsonResponse);
    }

    HttpRequest buildHttpRequest(
            @NonNull String queryString,
            LocalDate earliestDate,
            String programmingLanguage,
            Integer page) {

        return HttpRequest.newBuilder()
                .uri(buildRequestURI(queryString, earliestDate, programmingLanguage, page))
                .header("Accept", "application/vnd.github.v3+json")
                .build();
    }

    private URI buildRequestURI(
            @NonNull String queryString,
            LocalDate earliestDate,
            String programmingLanguage,
            Integer page) {

        String query = "q=" +  queryString;

        if (earliestDate != null) {
            query += "+created:>=" + earliestDate;
        }

        if  (programmingLanguage != null) {
            query += "+language:" +  programmingLanguage;
        }

        if  (page != null) {
            query += "&page=" + page;
        }

        query += "&per_page=" + PAGE_SIZE + "&sort=stars&order=desc";

        return new DefaultUriBuilderFactory()
                .builder()
                .scheme("https")
                .host(GITHUB_HOST)
                .path("/search/repositories")
                .query(query)
                .build();
    }

    private String sendHttpRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            response = client.send(request, ofString());
        }
        return response.body();
    }

    GithubRepositorySearchResponse parseJsonResponse(String jsonResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(jsonResponse, GithubRepositorySearchResponse.class);
    }
}
