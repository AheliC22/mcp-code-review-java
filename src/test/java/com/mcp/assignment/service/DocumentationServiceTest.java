package com.mcp.assignment.service;

import com.mcp.assignment.config.DocsConfig;
import com.mcp.assignment.model.DocumentSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for DocumentationService.
 * These tests verify the contract that candidates must implement in searchDocumentation().
 * When the stub is replaced with a correct implementation (see IMPLEMENTATION_GUIDE Step 5), all tests should pass.
 */
@DisplayName("DocumentationService tests")
class DocumentationServiceTest {

    @TempDir
    Path tempDir;

    private DocsConfig docsConfig;
    private DocumentationService documentationService;

    @BeforeEach
    void setUp() throws Exception {
        docsConfig = new DocsConfig();
        documentationService = new DocumentationService(docsConfig);

        // Create sample .md files for search
        Path doc1 = tempDir.resolve("api_guide.md");
        Files.writeString(doc1, "API Guide\nREST API endpoints.\nUse the API for integration.");
        Path doc2 = tempDir.resolve("setup.md");
        Files.writeString(doc2, "Setup Guide\nSetup your environment.\nAPI and setup instructions.");
        Path doc3 = tempDir.resolve("readme.md");
        Files.writeString(doc3, "README\nQuick start guide.");
    }

    @Test
    @Tag("exercise")
    @DisplayName("searchDocumentation returns success with results when implementation searches .md files")
    void searchDocumentation_shouldReturnSuccessWithResults() {
        docsConfig.setPath(tempDir.toString());

        Map<String, Object> result = documentationService.searchDocumentation("API");

        assertThat((Boolean) result.get("success")).as("searchDocumentation must return success=true when docs exist").isTrue();
        assertThat(result.get("query")).isEqualTo("API");
        assertThat(result.get("resultsCount")).isInstanceOf(Number.class);
        assertThat(result.get("results")).isInstanceOf(List.class);

        @SuppressWarnings("unchecked")
        List<DocumentSearchResult> results = (List<DocumentSearchResult>) result.get("results");
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(r -> r.getMatchCount() > 0);
        assertThat(results).allMatch(r -> r.getFileName().endsWith(".md"));
        assertThat(results).allMatch(r -> r.getContent() != null && !r.getContent().isEmpty());
    }

    @Test
    @Tag("exercise")
    @DisplayName("searchDocumentation is case-insensitive and returns results for lowercase query")
    void searchDocumentation_shouldBeCaseInsensitive() {
        docsConfig.setPath(tempDir.toString());

        Map<String, Object> result = documentationService.searchDocumentation("guide");

        assertThat((Boolean) result.get("success")).isTrue();
        @SuppressWarnings("unchecked")
        List<DocumentSearchResult> results = (List<DocumentSearchResult>) result.get("results");
        assertThat(results).hasSizeGreaterThanOrEqualTo(2); // api_guide.md, setup guide
    }

    @Test
    @Tag("exercise")
    @DisplayName("searchDocumentation sorts results by match count descending")
    void searchDocumentation_shouldSortByMatchCountDescending() {
        docsConfig.setPath(tempDir.toString());

        Map<String, Object> result = documentationService.searchDocumentation("guide");

        assertThat((Boolean) result.get("success")).isTrue();
        @SuppressWarnings("unchecked")
        List<DocumentSearchResult> results = (List<DocumentSearchResult>) result.get("results");
        for (int i = 1; i < results.size(); i++) {
            assertThat(results.get(i).getMatchCount())
                    .isLessThanOrEqualTo(results.get(i - 1).getMatchCount());
        }
    }

    @Test
    @DisplayName("searchDocumentation returns error when docs directory does not exist")
    void searchDocumentation_shouldReturnErrorWhenDirNotFound() {
        docsConfig.setPath(tempDir + "/nonexistent_subdir_12345");

        Map<String, Object> result = documentationService.searchDocumentation("anything");

        assertThat((Boolean) result.get("success")).isFalse();
        assertThat(result.get("error")).isNotNull();
    }
}
