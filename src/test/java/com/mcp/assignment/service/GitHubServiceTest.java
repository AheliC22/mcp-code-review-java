package com.mcp.assignment.service;

import com.mcp.assignment.config.GitHubConfig;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class GitHubServiceTest {

    @Test
    void serviceCanBeCreated() {
        WebClient webClient = mock(WebClient.class);
        GitHubConfig config = mock(GitHubConfig.class);

        GitHubService service = new GitHubService(webClient, config);

        assertThat(service).isNotNull();
    }
}