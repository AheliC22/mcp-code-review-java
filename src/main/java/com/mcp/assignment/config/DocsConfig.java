package com.mcp.assignment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class DocsConfig {

    private final Path docsPath;

    public DocsConfig(@Value("${docs.path:docs/}") String docsPath) {
        this.docsPath = Path.of(docsPath).toAbsolutePath().normalize();
    }

    public Path getDocsPath() {
        return docsPath;
    }
}