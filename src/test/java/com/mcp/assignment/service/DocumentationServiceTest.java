package com.mcp.assignment.service;

import com.mcp.assignment.config.DocsConfig;
import com.mcp.assignment.model.DocumentSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentationServiceTest {

    @TempDir
    Path tempDir;

    private DocumentationService documentationService;

    @BeforeEach
    void setUp() throws Exception {
        Files.writeString(
                tempDir.resolve("api_guide.md"),
                "API Guide\nREST API endpoints.\nUse the API for integration."
        );

        Files.writeString(
                tempDir.resolve("setup.md"),
                "Setup Guide\nSetup your environment.\nAPI and setup instructions."
        );

        Files.writeString(
                tempDir.resolve("readme.md"),
                "README\nQuick start guide."
        );

        documentationService = new DocumentationService(
                new DocsConfig(tempDir.toString())
        );
    }

    @Test
    void searchDocumentationReturnsMatchingResults() {
        List<DocumentSearchResult> results =
                documentationService.searchDocumentation("API");

        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(
                result -> result.getOccurrences() > 0
        );
        assertThat(results).allMatch(
                result -> result.getFileName().endsWith(".md")
        );
        assertThat(results).allMatch(
                result -> result.getContent() != null
        );
    }

    @Test
    void searchDocumentationIsCaseInsensitive() {
        List<DocumentSearchResult> results =
                documentationService.searchDocumentation("guide");

        assertThat(results).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void searchDocumentationSortsByRelevance() {
        List<DocumentSearchResult> results =
                documentationService.searchDocumentation("guide");

        for (int i = 1; i < results.size(); i++) {
            assertThat(results.get(i).getRelevance())
                    .isLessThanOrEqualTo(
                            results.get(i - 1).getRelevance()
                    );
        }
    }

    @Test
    void searchDocumentationReturnsEmptyListWhenNoMatches() {
        List<DocumentSearchResult> results =
                documentationService.searchDocumentation("xyz123");

        assertThat(results).isEmpty();
    }

    @Test
    void searchDocumentationRejectsEmptyKeyword() {
        org.assertj.core.api.Assertions.assertThatThrownBy(
                () -> documentationService.searchDocumentation("")
        ).isInstanceOf(IllegalArgumentException.class);
    }
}