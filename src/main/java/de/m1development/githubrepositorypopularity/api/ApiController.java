package de.m1development.githubrepositorypopularity.api;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {

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
        Map<String, Object> response = new HashMap<>();
        response.put("queryString", queryString);
        response.put("earliestDate", earliestDate);
        response.put("programmingLanguage", programmingLanguage);
        return ResponseEntity.ok(response);
    }
}
