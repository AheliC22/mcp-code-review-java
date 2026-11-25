package com.mcp.assignment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.assignment.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MCPServiceTest {

    private GitHubService gitHubService;
    private DocumentationService documentationService;
    private MCPService mcpService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        gitHubService = mock(GitHubService.class);
        documentationService = mock(DocumentationService.class);
        mcpService = new MCPService(gitHubService, documentationService);
        objectMapper = new ObjectMapper();
    }

    private MCPRequest createRequest(String method, Object params) {
        MCPRequest request = new MCPRequest();
        request.setJsonrpc("2.0");
        request.setMethod(method);
        request.setParams(params == null ? null : objectMapper.valueToTree(params));
        request.setId("1");
        return request;
    }

    @Test
    void toolsListReturnsThreeTools() {
        MCPResponse response = mcpService.handleRequest(
                createRequest("tools/list", null));

        assertThat(response.getError()).isNull();
        assertThat(response.getResult()).isNotNull();

        @SuppressWarnings("unchecked")
        Map<String, Object> result =
                (Map<String, Object>) response.getResult();

        @SuppressWarnings("unchecked")
        List<MCPTool> tools = (List<MCPTool>) result.get("tools");

        assertThat(tools).hasSize(3);
    }

    @Test
    void getRepositoryCallsGitHubService() {
        RepositoryInfo repository = mock(RepositoryInfo.class);
        when(gitHubService.getRepositoryInfo()).thenReturn(repository);

        MCPResponse response = mcpService.handleRequest(
                createRequest("tools/call",
                        Map.of("name", "get_repository",
                               "arguments", Map.of())));

        assertThat(response.getError()).isNull();
        verify(gitHubService).getRepositoryInfo();
    }

    @Test
    void getFileContentCallsGitHubService() {
        FileContent file = mock(FileContent.class);
        when(gitHubService.getFileContent("README.md")).thenReturn(file);

        MCPResponse response = mcpService.handleRequest(
                createRequest("tools/call",
                        Map.of("name", "get_file_content",
                               "arguments", Map.of("path", "README.md"))));

        assertThat(response.getError()).isNull();
        verify(gitHubService).getFileContent("README.md");
    }

    @Test
    void searchDocsCallsDocumentationService() {
        List<DocumentSearchResult> results = List.of(
                mock(DocumentSearchResult.class));

        when(documentationService.searchDocumentation("API"))
                .thenReturn(results);

        MCPResponse response = mcpService.handleRequest(
                createRequest("tools/call",
                        Map.of("name", "search_docs",
                               "arguments", Map.of("keyword", "API"))));

        assertThat(response.getError()).isNull();
        verify(documentationService).searchDocumentation("API");
    }

    @Test
    void unknownToolReturnsError() {
        MCPResponse response = mcpService.handleRequest(
                createRequest("tools/call",
                        Map.of("name", "unknown_tool",
                               "arguments", Map.of())));

        assertThat(response.getError()).isNotNull();
        assertThat(response.getError().getCode()).isEqualTo(-32602);
    }

    @Test
    void unknownMethodReturnsError() {
        MCPResponse response = mcpService.handleRequest(
                createRequest("unknown/method", null));

        assertThat(response.getError()).isNotNull();
        assertThat(response.getError().getCode()).isEqualTo(-32601);
    }
}