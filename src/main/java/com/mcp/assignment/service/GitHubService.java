package com.mcp.assignment.service;

import com.mcp.assignment.config.GitHubConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for GitHub API integration
 * Implements tools for repository access and file retrieval
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubService {
    
    private final GitHubConfig gitHubConfig;
    private final WebClient.Builder webClientBuilder;
    
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

    /**
     * Get repository information from GitHub.
     * TODO (Exercise): Implement this method - see IMPLEMENTATION_GUIDE.md Step 4.
     * Build GitHub API URL, use createWebClient(), make GET request, parse to RepositoryInfo,
     * return map with "success" (true/false), "repository" (RepositoryInfo), or "error" on failure.
     */
    public Map<String, Object> getRepository(String owner, String repo) {
        log.info("Fetching repository info for {}/{}", owner, repo);
        // TODO: Remove the stub below and implement - call GitHub API, parse response, return result map
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("error", "Not implemented - see IMPLEMENTATION_GUIDE.md Step 4");
        return result;
    }

    /**
     * Get file content from GitHub repository.
     * TODO (Exercise): Implement this method - see IMPLEMENTATION_GUIDE.md Step 4.
     * Build GitHub contents API URL, make GET request, decode Base64 content from response,
     * return map with "success", "path", "name", "content" (decoded), "size", or "error" on failure.
     */
    public Map<String, Object> getFileContent(String owner, String repo, String path) {
        log.info("Fetching file content for {}/{}/{}", owner, repo, path);
        // TODO: Remove the stub below and implement - call GitHub API, decode Base64 content, return result map
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("error", "Not implemented - see IMPLEMENTATION_GUIDE.md Step 4");
        return result;
    }
    
    /**
     * Create WebClient with GitHub authentication
     */
    private WebClient createWebClient() {
        return webClientBuilder
                .defaultHeader("Authorization", "Bearer " + gitHubConfig.getToken())
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }
}

