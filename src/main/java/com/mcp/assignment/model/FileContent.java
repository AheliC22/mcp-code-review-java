package com.mcp.assignment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * GitHub File Content
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileContent {
    
    private String name;
    
    private String path;
    
    private String sha;
    
    private Long size;
    
    private String type;
    
    private String content;
    
    private String encoding;
    
    @JsonProperty("download_url")
    private String downloadUrl;
}

