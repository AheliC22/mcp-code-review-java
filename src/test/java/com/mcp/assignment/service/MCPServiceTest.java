package com.mcp.assignment.service;

import com.mcp.assignment.model.MCPRequest;
import com.mcp.assignment.model.MCPResponse;
import com.mcp.assignment.model.MCPTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MCPService.
 * Verifies that tool routing (tools/list, tools/call) works and delegates to GitHubService and DocumentationService.
 * When candidates implement those services, the full flow will work.
 */
@DisplayName("MCPService tests")
class MCPServiceTest {

    private GitHubService gitHubService;
    private DocumentationService documentationService;
    private MCPService mcpService;

    @BeforeEach
    void setUp() {
        gitHubService = mock(GitHubService.class);
        documentationService = mock(DocumentationService.class);
        mcpService = new MCPService(gitHubService, documentationService);
    }

    @Test
    @DisplayName("handleRequest with tools/list returns list of tools")
    void handleRequest_toolsList_returnsTools() {
        MCPRequest request = new MCPRequest();
        request.setMethod("tools/list");
        request.setParams(null);
        request.setId("1");

        MCPResponse response = mcpService.handleRequest(request);

        assertThat(response.getError()).isNull();
        assertThat(response.getResult()).isNotNull();
        @SuppressWarnings("unchecked")
        List<MCPTool> tools = (List<MCPTool>) response.getResult();
        assertThat(tools).hasSize(3); // get_repository, get_file_content, search_docs
    }

    @Test
    @DisplayName("handleRequest with tools/call get_repository delegates to GitHubService")
    void handleRequest_getRepository_delegatesToGitHubService() {
        Map<String, Object> repoResult = new HashMap<>();
        repoResult.put("success", true);
        repoResult.put("repository", "mock-repo-info");
        when(gitHubService.getRepository(eq("spring-projects"), eq("spring-boot"))).thenReturn(repoResult);

        MCPRequest request = new MCPRequest();
        request.setMethod("tools/call");
        request.setParams(Map.of(
                "name", "get_repository",
                "arguments", Map.of("owner", "spring-projects", "repo", "spring-boot")
        ));
        request.setId("2");

        MCPResponse response = mcpService.handleRequest(request);

        assertThat(response.getError()).isNull();
        assertThat(response.getResult()).isEqualTo(repoResult);
        verify(gitHubService).getRepository("spring-projects", "spring-boot");
    }

    @Test
    @DisplayName("handleRequest with tools/call get_file_content delegates to GitHubService")
    void handleRequest_getFileContent_delegatesToGitHubService() {
        Map<String, Object> fileResult = new HashMap<>();
        fileResult.put("success", true);
        fileResult.put("content", "file content");
        when(gitHubService.getFileContent(eq("owner"), eq("repo"), eq("README.md"))).thenReturn(fileResult);

        MCPRequest request = new MCPRequest();
        request.setMethod("tools/call");
        request.setParams(Map.of(
                "name", "get_file_content",
                "arguments", Map.of("owner", "owner", "repo", "repo", "path", "README.md")
        ));
        request.setId("3");

        MCPResponse response = mcpService.handleRequest(request);

        assertThat(response.getError()).isNull();
        assertThat(response.getResult()).isEqualTo(fileResult);
        verify(gitHubService).getFileContent("owner", "repo", "README.md");
    }

    @Test
    @DisplayName("handleRequest with tools/call search_docs delegates to DocumentationService")
    void handleRequest_searchDocs_delegatesToDocumentationService() {
        Map<String, Object> searchResult = new HashMap<>();
        searchResult.put("success", true);
        searchResult.put("resultsCount", 2);
        when(documentationService.searchDocumentation(eq("API"))).thenReturn(searchResult);

        MCPRequest request = new MCPRequest();
        request.setMethod("tools/call");
        request.setParams(Map.of(
                "name", "search_docs",
                "arguments", Map.of("query", "API")
        ));
        request.setId("4");

        MCPResponse response = mcpService.handleRequest(request);

        assertThat(response.getError()).isNull();
        assertThat(response.getResult()).isEqualTo(searchResult);
        verify(documentationService).searchDocumentation("API");
    }

    @Test
    @DisplayName("handleRequest with unknown method returns error")
    void handleRequest_unknownMethod_returnsError() {
        MCPRequest request = new MCPRequest();
        request.setMethod("unknown/method");
        request.setId("5");

        MCPResponse response = mcpService.handleRequest(request);

        assertThat(response.getError()).isNotNull();
        assertThat(response.getResult()).isNull();
    }
}
