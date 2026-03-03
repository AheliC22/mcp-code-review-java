package com.mcp.assignment.service;

import com.mcp.assignment.config.DocsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for searching local documentation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentationService {
    
    private final DocsConfig docsConfig;
    
    /**
     * Search documentation files for the given query.
     * TODO (Exercise): Implement this method - see IMPLEMENTATION_GUIDE.md Step 5.
     * Get docs path from config, walk file tree (Files.walk), filter .md files,
     * search each file (case-insensitive), count matches, sort by relevance (match count descending),
     * return map with "success", "query", "resultsCount", "results" (List of DocumentSearchResult).
     */
    public Map<String, Object> searchDocumentation(String query) {
        log.info("Searching documentation for: {}", query);
        // TODO: Remove the stub below and implement - walk docs path, filter .md, search content, sort by match count
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("error", "Not implemented - see IMPLEMENTATION_GUIDE.md Step 5");
        return result;
    }
}

