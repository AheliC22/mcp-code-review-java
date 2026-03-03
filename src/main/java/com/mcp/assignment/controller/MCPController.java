package com.mcp.assignment.controller;

import com.mcp.assignment.model.MCPRequest;
import com.mcp.assignment.model.MCPResponse;
import com.mcp.assignment.service.MCPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * MCP Server Controller
 * Handles incoming MCP protocol requests
 */
@Slf4j
@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
public class MCPController {
    
    private final MCPService mcpService;
    
    /**
     * Main MCP endpoint for handling tool calls
     */
    @PostMapping("/call")
    public ResponseEntity<MCPResponse> handleMCPCall(@RequestBody MCPRequest request) {
        log.info("Received MCP request: method={}, id={}", request.getMethod(), request.getId());
        
        try {
            MCPResponse response = mcpService.handleRequest(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error handling MCP request", e);
            return ResponseEntity.ok(
                MCPResponse.error(-32603, "Internal error: " + e.getMessage(), request.getId())
            );
        }
    }
    
    /**
     * List available MCP tools
     */
    @GetMapping("/tools")
    public ResponseEntity<MCPResponse> listTools() {
        log.info("Listing available MCP tools");
        
        try {
            MCPResponse response = mcpService.listTools();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error listing tools", e);
            return ResponseEntity.ok(
                MCPResponse.error(-32603, "Internal error: " + e.getMessage(), null)
            );
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("MCP Server is running!");
    }
}

