package de.m1development.githubrepositorypopularity.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

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

}