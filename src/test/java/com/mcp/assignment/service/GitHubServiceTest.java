package com.mcp.assignment.service;

import com.mcp.assignment.config.GitHubConfig;
import com.mcp.assignment.model.FileContent;
import com.mcp.assignment.model.RepositoryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GitHubService.
 * These tests verify the contract that candidates must implement in getRepository() and getFileContent().
 * When the stub is replaced with a correct implementation (see IMPLEMENTATION_GUIDE Step 4), all tests should pass.
 */
@DisplayName("GitHubService tests")
class GitHubServiceTest {

    private GitHubConfig gitHubConfig;
    private WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private GitHubService gitHubService;

    @BeforeEach
    void setUp() {
        gitHubConfig = mock(GitHubConfig.class);
        var apiConfig = new GitHubConfig.Api();
        apiConfig.setBaseUrl("https://api.github.com");
        when(gitHubConfig.getApi()).thenReturn(apiConfig);
        when(gitHubConfig.getToken()).thenReturn("test-token");

        webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        when(webClientBuilder.defaultHeader(anyString(), anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        gitHubService = new GitHubService(gitHubConfig, webClientBuilder);
    }

    @Test
    @Tag("exercise")
    @DisplayName("getRepository returns success with repository info when implementation calls GitHub API and parses response")
    void getRepository_shouldReturnSuccessWithRepositoryInfo() {
        // Simulate what the implementation should do: call GitHub API and get RepositoryInfo
        RepositoryInfo expectedRepo = new RepositoryInfo();
        expectedRepo.setId(12345L);
        expectedRepo.setName("spring-boot");
        expectedRepo.setFullName("spring-projects/spring-boot");
        expectedRepo.setDescription("Spring Boot");
        expectedRepo.setLanguage("Java");
        expectedRepo.setStargazersCount(70000);

        // Mock the WebClient chain: get().uri(...).retrieve().bodyToMono(RepositoryInfo.class).timeout(...).block()
        var requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("/repos/"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RepositoryInfo.class)).thenReturn(Mono.just(expectedRepo));

        Map<String, Object> result = gitHubService.getRepository("spring-projects", "spring-boot");

        // Contract: implementation must return success=true and repository object when API returns data
        assertThat((Boolean) result.get("success")).as("getRepository must return success=true when API returns repository").isTrue();
        assertThat(result.get("repository")).isInstanceOf(RepositoryInfo.class);
        RepositoryInfo repo = (RepositoryInfo) result.get("repository");
        assertThat(repo.getFullName()).isEqualTo("spring-projects/spring-boot");
        assertThat(repo.getName()).isEqualTo("spring-boot");
    }

    @Test
    @Tag("exercise")
    @DisplayName("getFileContent returns success with decoded content when implementation decodes Base64")
    void getFileContent_shouldReturnSuccessWithDecodedContent() {
        String originalContent = "Hello, World!\nLine 2";
        String base64Content = Base64.getEncoder().encodeToString(originalContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        FileContent fileContentResponse = new FileContent();
        fileContentResponse.setName("README.md");
        fileContentResponse.setPath("README.md");
        fileContentResponse.setContent(base64Content.replaceAll("\\s", ""));
        fileContentResponse.setSize((long) originalContent.length());

        var requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("/contents/"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FileContent.class)).thenReturn(Mono.just(fileContentResponse));

        Map<String, Object> result = gitHubService.getFileContent("owner", "repo", "README.md");

        // Contract: implementation must decode Base64 and return success=true with content
        assertThat((Boolean) result.get("success")).as("getFileContent must return success=true and decode Base64 content").isTrue();
        assertThat(result).containsKeys("path", "content", "name");
        assertThat(result.get("content")).isEqualTo(originalContent);
        assertThat(result.get("path")).isEqualTo("README.md");
    }

    @Test
    @Tag("exercise")
    @DisplayName("getRepository returns error map on exception when implementation handles errors")
    void getRepository_shouldReturnErrorMapOnFailure() {
        var requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RepositoryInfo.class))
                .thenReturn(Mono.error(new RuntimeException("Not found")));

        Map<String, Object> result = gitHubService.getRepository("unknown", "nonexistent");

        // Contract: implementation must catch exceptions and return success=false with error message
        assertThat((Boolean) result.get("success")).isFalse();
        assertThat(result.get("error")).isEqualTo("Not found");
    }
}
