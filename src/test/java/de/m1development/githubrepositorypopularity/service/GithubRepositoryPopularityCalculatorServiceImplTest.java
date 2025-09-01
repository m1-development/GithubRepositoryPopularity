package de.m1development.githubrepositorypopularity.service;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryPopularity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GithubRepositoryPopularityCalculatorServiceImplTest {

    private GithubRepositoryPopularityCalculatorService sut;

    @BeforeEach
    void setup() {
        sut = new GithubRepositoryPopularityCalculatorServiceImpl();
    }

    @Test
    void calculatePopularityForRepositories_requiredParameterOnly() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = null;
        String programmingLanguage = null;

        // when
        List<GithubRepositoryPopularity> list = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void calculatePopularityForRepositories_requiredParameterWithEarliestDate() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = LocalDate.of(2025,9,1);
        String programmingLanguage = null;

        // when
        List<GithubRepositoryPopularity> list = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void calculatePopularityForRepositories_requiredParameterWithProgrammingLanguage() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = null;
        String programmingLanguage = "Java";

        // when
        List<GithubRepositoryPopularity> list = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void calculatePopularityForRepositories_withAllParameters() {
        // given
        String queryString = "Test";
        LocalDate earliestDate = LocalDate.of(2025,9,1);
        String programmingLanguage = "Java";

        // when
        List<GithubRepositoryPopularity> list = sut.calculatePopularityForRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(list);
        assertEquals(1, list.size());
    }
}