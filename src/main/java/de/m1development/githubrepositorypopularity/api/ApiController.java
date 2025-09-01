package de.m1development.githubrepositorypopularity.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        String response = "Hello World";
        return ResponseEntity.ok(response);
    }
}
