package com.mcp.assignment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitHubConfig {

    @Value("${github.token}")
    private String token;

    @Value("${github.repo.owner}")
    private String owner;

    @Value("${github.repo.name}")
    private String repository;

    @Value("${github.api.base-url}")
    private String baseUrl;

    public String getToken() {
        return token;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepository() {
        return repository;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}