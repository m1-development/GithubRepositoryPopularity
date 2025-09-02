package de.m1development.githubrepositorypopularity.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiControllerE2ETest {

    @LocalServerPort
    private Integer port;

    @Test
    void testHomePath() {
        Response response = RestAssured
                .given()
                .log().all()
                .baseUri("http://localhost")
                .port(port)
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .extract().response();

        assertEquals("Hello World", response.asString());
    }

    @Test
    public void testCalculatePopularity_withAllParams() {
        Response response = RestAssured
                .given()
                .log().all()
                .baseUri("http://localhost")
                .port(port)
                .pathParam("queryString", "test")
                .queryParam("earliestDate", "2025-08-30")
                .queryParam("programmingLanguage", "Java")
                .when()
                .get("/calculatePopularity/{queryString}")
                .then()
                .statusCode(200)
                .extract().response();

        assertEquals("test", response.jsonPath().getString("query"));
        assertEquals("2025-08-30", response.jsonPath().getString("earliest_date"));
        assertEquals("Java", response.jsonPath().getString("programming_language"));

        List<LinkedHashMap> matchingRepositories = response.jsonPath().getList("matching_repositories");

        assertNotNull(matchingRepositories);
        assertTrue(matchingRepositories.size() > 1);
    }
}