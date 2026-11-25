
package com.mcp.assignment.service;

import com.mcp.assignment.config.DocsConfig;
import com.mcp.assignment.model.DocumentSearchResult;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Service
public class DocumentationService {

    private final DocsConfig docsConfig;

    public DocumentationService(DocsConfig docsConfig) {
        this.docsConfig = docsConfig;
    }

    public List<DocumentSearchResult> searchDocumentation(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword cannot be empty");
        }

        List<DocumentSearchResult> results = new ArrayList<>();
        Path docsPath = docsConfig.getDocsPath();

        if (!Files.exists(docsPath)) {
            return results;
        }

        try (Stream<Path> files = Files.walk(docsPath)) {
            files.filter(Files::isRegularFile)
                    .filter(path -> path.toString()
                            .toLowerCase().endsWith(".md"))
                    .forEach(path -> searchFile(path, keyword, results));
        } catch (IOException e) {
            throw new RuntimeException("Failed to search documentation", e);
        }

        results.sort(Comparator.comparingInt(
                DocumentSearchResult::getRelevance).reversed());

        return results;
    }

    private void searchFile(
            Path file, String keyword,
            List<DocumentSearchResult> results) {

        try {
            String content = Files.readString(file);
            String lowerContent = content.toLowerCase();
            String lowerKeyword = keyword.toLowerCase();

            int occurrences = 0;
            int index = 0;

            while ((index = lowerContent.indexOf(lowerKeyword, index)) != -1) {
                occurrences++;
                index += lowerKeyword.length();
            }

            if (occurrences > 0) {
                DocumentSearchResult result = new DocumentSearchResult();
                result.setFileName(file.getFileName().toString());
                result.setFilePath(file.toString());
                result.setContent(content);
                result.setOccurrences(occurrences);

                int relevance = occurrences * 10;

                if (file.getFileName().toString()
                        .toLowerCase().contains(lowerKeyword)) {
                    relevance += 10;
                }

                result.setRelevance(relevance);
                results.add(result);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + file, e);
        }
    }
}
