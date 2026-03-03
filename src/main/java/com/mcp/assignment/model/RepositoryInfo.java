package com.mcp.assignment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * GitHub Repository Information
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryInfo {
    
    private Long id;
    
    private String name;
    
    @JsonProperty("full_name")
    private String fullName;
    
    private String description;
    
    @JsonProperty("html_url")
    private String htmlUrl;
    
    private String language;
    
    @JsonProperty("stargazers_count")
    private Integer stargazersCount;
    
    @JsonProperty("forks_count")
    private Integer forksCount;
    
    @JsonProperty("open_issues_count")
    private Integer openIssuesCount;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("updated_at")
    private String updatedAt;
    
    @JsonProperty("default_branch")
    private String defaultBranch;
}

