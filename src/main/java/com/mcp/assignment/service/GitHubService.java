
package com.mcp.assignment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mcp.assignment.config.GitHubConfig;
import com.mcp.assignment.model.FileContent;
import com.mcp.assignment.model.RepositoryInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class GitHubService {

    private final WebClient webClient;
    private final GitHubConfig gitHubConfig;

    public GitHubService(WebClient webClient, GitHubConfig gitHubConfig) {
        this.webClient = webClient;
        this.gitHubConfig = gitHubConfig;
    }

    private WebClient.RequestHeadersSpec<?> createRequest(String url) {
        return webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + gitHubConfig.getToken())
                .header(HttpHeaders.ACCEPT,
                        "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28");
    }

    public RepositoryInfo getRepositoryInfo() {

        String url = gitHubConfig.getBaseUrl()
                + "/repos/"
                + gitHubConfig.getOwner()
                + "/"
                + gitHubConfig.getRepository();

        JsonNode response = createRequest(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(
                                        new RuntimeException(
                                                "GitHub API error: "
                                                        + clientResponse.statusCode()
                                                        + " - " + body))))
                .bodyToMono(JsonNode.class)
                .block(java.time.Duration.ofSeconds(30));

        if (response == null) {
            throw new RuntimeException("Empty response from GitHub API");
        }

        RepositoryInfo repository = new RepositoryInfo();

        repository.setName(response.path("name").asText());
        repository.setFullName(response.path("full_name").asText());
        repository.setDescription(response.path("description").asText(""));
        repository.setHtmlUrl(response.path("html_url").asText());
        repository.setDefaultBranch(response.path("default_branch").asText());
        repository.setLanguage(response.path("language").asText(""));
        repository.setPrivate(response.path("private").asBoolean());

        return repository;
    }

    public FileContent getFileContent(String filePath) {

        String url = gitHubConfig.getBaseUrl()
                + "/repos/"
                + gitHubConfig.getOwner()
                + "/"
                + gitHubConfig.getRepository()
                + "/contents/"
                + filePath.replace(" ", "%20");

        JsonNode response = createRequest(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(
                                        new RuntimeException(
                                                "GitHub API error: "
                                                        + clientResponse.statusCode()
                                                        + " - " + body))))
                .bodyToMono(JsonNode.class)
                .block(java.time.Duration.ofSeconds(30));

        if (response == null) {
            throw new RuntimeException("Empty response from GitHub API");
        }

        String encodedContent = response.path("content").asText("");
        String decodedContent = new String(
                Base64.getMimeDecoder().decode(encodedContent),
                StandardCharsets.UTF_8
        );

        FileContent file = new FileContent();

        file.setName(response.path("name").asText());
        file.setPath(response.path("path").asText());
        file.setContent(decodedContent);
        file.setSha(response.path("sha").asText());
        file.setHtmlUrl(response.path("html_url").asText());

        return file;
    }
}
