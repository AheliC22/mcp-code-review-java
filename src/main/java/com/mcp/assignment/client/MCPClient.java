package com.mcp.assignment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.assignment.model.MCPRequest;
import com.mcp.assignment.model.MCPResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simple MCP Client for testing the MCP Server
 * 
 * Usage:
 * 1. Make sure the server is running: mvn spring-boot:run
 * 2. Run this client: java -cp target/classes com.mcp.assignment.client.MCPClient
 */
public class MCPClient {
    
    private static final String BASE_URL = "http://localhost:8080/mcp";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    
    public static void main(String[] args) {
        MCPClient client = new MCPClient();
        
        System.out.println("===========================================");
        System.out.println("🧪 MCP Client - Testing MCP Server");
        System.out.println("===========================================\n");
        
        try {
            // Test 1: Health Check
            client.testHealthCheck();
            
            // Test 2: List Tools
            client.testListTools();
            
            // Test 3: Get Repository
            client.testGetRepository();
            
            // Test 4: Get File Content
            client.testGetFileContent();
            
            // Test 5: Search Documentation
            client.testSearchDocs();
            
            System.out.println("\n===========================================");
            System.out.println("✅ All Tests Completed!");
            System.out.println("===========================================");
            
        } catch (Exception e) {
            System.err.println("\n❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test health check endpoint
     */
    private void testHealthCheck() throws Exception {
        System.out.println("📋 Test 1: Health Check");
        System.out.println("─────────────────────────────────────────");
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/health"))
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
        
        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
        System.out.println("✅ Health check passed!\n");
    }
    
    /**
     * Test listing available tools
     */
    private void testListTools() throws Exception {
        System.out.println("📋 Test 2: List Available Tools");
        System.out.println("─────────────────────────────────────────");
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tools"))
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
        
        MCPResponse mcpResponse = objectMapper.readValue(response.body(), MCPResponse.class);
        
        System.out.println("Response:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(mcpResponse));
        System.out.println("✅ List tools test passed!\n");
    }
    
    /**
     * Test get_repository tool
     */
    private void testGetRepository() throws Exception {
        System.out.println("📋 Test 3: Get Repository Information");
        System.out.println("─────────────────────────────────────────");
        
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("owner", "spring-projects");
        arguments.put("repo", "spring-boot");
        
        MCPResponse response = callTool("get_repository", arguments);
        
        System.out.println("Response:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(response));
        System.out.println("✅ Get repository test passed!\n");
    }
    
    /**
     * Test get_file_content tool
     */
    private void testGetFileContent() throws Exception {
        System.out.println("📋 Test 4: Get File Content");
        System.out.println("─────────────────────────────────────────");
        
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("owner", "spring-projects");
        arguments.put("repo", "spring-boot");
        arguments.put("path", "README.md");
        
        MCPResponse response = callTool("get_file_content", arguments);
        
        System.out.println("Response:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(response));
        System.out.println("✅ Get file content test passed!\n");
    }
    
    /**
     * Test search_docs tool
     */
    private void testSearchDocs() throws Exception {
        System.out.println("📋 Test 5: Search Documentation");
        System.out.println("─────────────────────────────────────────");
        
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("query", "API");
        
        MCPResponse response = callTool("search_docs", arguments);
        
        System.out.println("Response:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(response));
        System.out.println("✅ Search docs test passed!\n");
    }
    
    /**
     * Call an MCP tool
     */
    private MCPResponse callTool(String toolName, Map<String, Object> arguments) throws Exception {
        MCPRequest mcpRequest = new MCPRequest();
        mcpRequest.setJsonrpc("2.0");
        mcpRequest.setMethod("tools/call");
        mcpRequest.setId(UUID.randomUUID().toString());
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", toolName);
        params.put("arguments", arguments);
        mcpRequest.setParams(params);
        
        String requestBody = objectMapper.writeValueAsString(mcpRequest);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/call"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
        
        return objectMapper.readValue(response.body(), MCPResponse.class);
    }
}

