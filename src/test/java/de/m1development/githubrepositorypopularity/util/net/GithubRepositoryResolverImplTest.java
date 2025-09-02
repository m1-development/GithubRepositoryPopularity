package de.m1development.githubrepositorypopularity.util.net;

import de.m1development.githubrepositorypopularity.model.GithubRepositoryPopularity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GithubRepositoryResolverImplTest {

    private GithubRepositoryResolver sut;

    @BeforeEach
    void setUp() {
        sut = new GithubRepositoryResolverImpl();
    }

    @Test
    void resolveMatchingGithubRepositories() {
        // given
        String queryString = "test";
        LocalDate earliestDate = LocalDate.of(2024,1,1);
        String programmingLanguage = "python";

        // when
        List<GithubRepositoryPopularity> response = sut.resolveMatchingGithubRepositories(queryString, earliestDate, programmingLanguage);

        // then
        assertNotNull(response);
        assertEquals(0, response.size());
    }
}