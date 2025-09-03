package de.m1development.githubrepositorypopularity.api;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import de.m1development.githubrepositorypopularity.service.GithubRepositoryPopularityCalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
public class ApiController implements ErrorController {

    private final GithubRepositoryPopularityCalculatorService calculatorService;

    @Autowired
    public ApiController(GithubRepositoryPopularityCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    /**
     * @return Description of the API and available endpoints
     *
     * @status 200
     */
    @GetMapping("/")
    public ResponseEntity<HttpHomeResponse> home() {
        HttpHomeResponse httpHomeResponse = new HttpHomeResponse();
        httpHomeResponse.setDescription("Github Repository Popularity Calculator");
        httpHomeResponse.getEndpoints().put("Home with available endpoints", "GET /");
        httpHomeResponse.getEndpoints().put("Calculate popularity score for github repositories", "GET /calculatePopularity/{queryString}?earliestDate={earliestDate}&programmingLanguage={programmingLanguage}");

        return ResponseEntity.ok(httpHomeResponse);
    }

    /**
     * @param queryString           Best match query string for repositories on github
     * @param earliestDate          Optional filter to get repositories created on this date (format: YYYY-MM-DD)
     * @param programmingLanguage   Optional filter to get repositories using the specified programming language
     *
     * @return                      JSON with all matching repositories and their calculated popularity score
     *
     * @status                      200 if successful, 500 if an error occurred
     *
     */
    @GetMapping("/calculatePopularity/{queryString}")
    public ResponseEntity<Object> calculatePopularity(
            @PathVariable String queryString,
            @RequestParam(required = false) String earliestDate,
            @RequestParam(required = false) String programmingLanguage
    ) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedEarliestDate = earliestDate != null ? LocalDate.parse(earliestDate, formatter) : null;

            List<GithubRepositoryItem> repositories = calculatorService.calculatePopularityForRepositories(queryString, parsedEarliestDate, programmingLanguage);

            CalculatePopularityResponse calculatePopularityResponse = CalculatePopularityResponse.builder()
                    .query(queryString)
                    .earliestDate(parsedEarliestDate)
                    .programmingLanguage(programmingLanguage)
                    .matchingRepositoryCount(repositories.size())
                    .repositories(repositories)
                    .build();

            return ResponseEntity.ok(calculatePopularityResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/error")
    public String error() {
        return "Internal Server Error";
    }
}
