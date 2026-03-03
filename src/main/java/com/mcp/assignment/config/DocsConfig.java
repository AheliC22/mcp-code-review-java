package com.mcp.assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for documentation settings
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "docs")
public class DocsConfig {
    
    private String path = "docs/";
}

