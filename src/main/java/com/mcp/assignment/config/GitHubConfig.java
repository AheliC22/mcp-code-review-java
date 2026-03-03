package com.mcp.assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for GitHub API settings
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "github")
public class GitHubConfig {
    
    private String token;
    
    private Repo repo = new Repo();
    
    private Api api = new Api();
    
    @Data
    public static class Repo {
        private String owner;
        private String name;
    }
    
    @Data
    public static class Api {
        private String baseUrl = "https://api.github.com";
    }
}

