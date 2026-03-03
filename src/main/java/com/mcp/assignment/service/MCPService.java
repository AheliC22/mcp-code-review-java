package com.mcp.assignment.service;

import com.mcp.assignment.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Main MCP Service that handles protocol requests and routes to appropriate tools
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MCPService {
    
    private final GitHubService gitHubService;
    private final DocumentationService documentationService;

    /**
     * Handle incoming MCP request
     */
    public MCPResponse handleRequest(MCPRequest request) {
        String method = request.getMethod();
        Map<String, Object> params = request.getParams();
        
        try {
            Object result = switch (method) {
                case "tools/list" -> getTools();
                case "tools/call" -> callTool(params);
                default -> throw new IllegalArgumentException("Unknown method: " + method);
            };
            
            return MCPResponse.success(result, request.getId());
        } catch (Exception e) {
            log.error("Error handling request: {}", method, e);
            return MCPResponse.error(-32603, e.getMessage(), request.getId());
        }
    }
    
    /**
     * List all available tools
     */
    public MCPResponse listTools() {
        Map<String, Object> result = new HashMap<>();
        result.put("tools", getTools());
        return MCPResponse.success(result, null);
    }
    
    /**
     * Get list of available MCP tools
     */
    private List<MCPTool> getTools() {
        List<MCPTool> tools = new ArrayList<>();
        
        // Tool 1: get_repository
        tools.add(MCPTool.builder()
                .name("get_repository")
                .description("Get information about a GitHub repository")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "owner", Map.of("type", "string", "description", "Repository owner"),
                                "repo", Map.of("type", "string", "description", "Repository name")
                        ),
                        "required", List.of("owner", "repo")
                ))
                .build());
        
        // Tool 2: get_file_content
        tools.add(MCPTool.builder()
                .name("get_file_content")
                .description("Get content of a file from GitHub repository")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "owner", Map.of("type", "string", "description", "Repository owner"),
                                "repo", Map.of("type", "string", "description", "Repository name"),
                                "path", Map.of("type", "string", "description", "File path in repository")
                        ),
                        "required", List.of("owner", "repo", "path")
                ))
                .build());
        
        // Tool 3: search_docs
        tools.add(MCPTool.builder()
                .name("search_docs")
                .description("Search local documentation files for keywords")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "query", Map.of("type", "string", "description", "Search query")
                        ),
                        "required", List.of("query")
                ))
                .build());
        
        return tools;
    }
    
    /**
     * Call a specific tool with given arguments
     */
    @SuppressWarnings("unchecked")
    private Object callTool(Map<String, Object> params) {
        String toolName = (String) params.get("name");
        Map<String, Object> arguments = (Map<String, Object>) params.get("arguments");
        
        log.info("Calling tool: {} with arguments: {}", toolName, arguments);
        
        return switch (toolName) {
            case "get_repository" -> gitHubService.getRepository(
                    (String) arguments.get("owner"),
                    (String) arguments.get("repo")
            );
            case "get_file_content" -> gitHubService.getFileContent(
                    (String) arguments.get("owner"),
                    (String) arguments.get("repo"),
                    (String) arguments.get("path")
            );
            case "search_docs" -> documentationService.searchDocumentation(
                    (String) arguments.get("query")
            );
            default -> throw new IllegalArgumentException("Unknown tool: " + toolName);
        };
    }
}

