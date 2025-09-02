package de.m1development.githubrepositorypopularity.api;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryItem;
import de.m1development.githubrepositorypopularity.service.GithubRepositoryPopularityCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiController {

    private final GithubRepositoryPopularityCalculatorService calculatorService;

    @Autowired
    public ApiController(GithubRepositoryPopularityCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        String response = "Hello World";
        return ResponseEntity.ok(response);
    }

    /**
     * @param queryString           Best match query string for repositories on github
     * @param earliestDate          Optional filter to get repositories created on this date
     * @param programmingLanguage   Optional filter to get repositories using the specified programming language
     *
     * @return                      JSON with all matching repositories and their calculated popularity score
     */
    @GetMapping("/calculatePopularity/{queryString}")
    public ResponseEntity<Map<String, Object>> calculatePopularity(
            @PathVariable String queryString,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate earliestDate,
            @RequestParam(required = false) String programmingLanguage
    ) {
        List<GithubRepositoryItem> repositories = calculatorService.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        Map<String, Object> response = new HashMap<>();
        response.put("query", queryString);
        response.put("earliest_date", earliestDate);
        response.put("programming_language", programmingLanguage);
        response.put("matching_repositories", repositories);
        return ResponseEntity.ok(response);
    }
}
