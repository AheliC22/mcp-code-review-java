
package com.mcp.assignment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.assignment.model.*;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MCPService {

    private final GitHubService gitHubService;
    private final DocumentationService documentationService;
    private final ObjectMapper objectMapper;

    public MCPService(
            GitHubService gitHubService,
            DocumentationService documentationService) {

        this.gitHubService = gitHubService;
        this.documentationService = documentationService;
        this.objectMapper = new ObjectMapper();
    }

    public List<MCPTool> getTools() {

        List<MCPTool> tools = new ArrayList<>();

        tools.add(new MCPTool(
                "get_repository",
                "Get information about the configured GitHub repository.",
                createSchema(Map.of(), List.of())
        ));

        tools.add(new MCPTool(
                "get_file_content",
                "Read a file from the configured GitHub repository.",
                createSchema(
                        Map.of("path", Map.of("type", "string")),
                        List.of("path")
                )
        ));

        tools.add(new MCPTool(
                "search_docs",
                "Search local Markdown documentation using keywords.",
                createSchema(
                        Map.of("keyword", Map.of("type", "string")),
                        List.of("keyword")
                )
        ));

        return tools;
    }

    private Map<String, Object> createSchema(
            Map<String, Object> properties,
            List<String> required) {

        Map<String, Object> schema = new LinkedHashMap<>();

        schema.put("type", "object");
        schema.put("properties", properties);
        schema.put("required", required);

        return schema;
    }

    public MCPResponse handleRequest(MCPRequest request) {

        MCPResponse response = new MCPResponse();
        response.setId(request.getId());

        if (!"2.0".equals(request.getJsonrpc())
                || request.getMethod() == null) {

            response.setError(
                    new MCPError(-32600, "Invalid request")
            );

            return response;
        }

        try {

            switch (request.getMethod()) {

                case "tools/list":
                    response.setResult(
                            Map.of("tools", getTools())
                    );
                    break;

                case "tools/call":
                    response.setResult(
                            callTool(request.getParams())
                    );
                    break;

                default:
                    response.setError(
                            new MCPError(-32601, "Method not found")
                    );
            }

        } catch (IllegalArgumentException e) {

            response.setError(
                    new MCPError(-32602, e.getMessage())
            );

        } catch (Exception e) {

            response.setError(
                    new MCPError(
                            -32000,
                            "Tool execution failed: " + e.getMessage()
                    )
            );
        }

        return response;
    }

    private Map<String, Object> callTool(JsonNode params)
            throws JsonProcessingException {

        if (params == null || params.isNull()) {
            throw new IllegalArgumentException(
                    "Missing parameters"
            );
        }

        String name = params.path("name").asText("");
        JsonNode arguments = params.path("arguments");

        Object result;

        switch (name) {

            case "get_repository":

                result = gitHubService.getRepositoryInfo();
                break;

            case "get_file_content":

                String path = arguments.path("path").asText("");

                if (path.isBlank()) {
                    throw new IllegalArgumentException(
                            "File path is required"
                    );
                }

                result = gitHubService.getFileContent(path);
                break;

            case "search_docs":

                String keyword = arguments.path("keyword").asText("");

                if (keyword.isBlank()) {
                    throw new IllegalArgumentException(
                            "Search keyword is required"
                    );
                }

                result = documentationService.searchDocumentation(keyword);
                break;

            default:

                throw new IllegalArgumentException(
                        "Unknown tool: " + name
                );
        }

        String readableResult =
                objectMapper.writeValueAsString(result);

        return Map.of(
                "content",
                List.of(
                        Map.of(
                                "type", "text",
                                "text", readableResult
                        )
                ),
                "isError", false,
                "structuredContent", result
        );
    }
}
