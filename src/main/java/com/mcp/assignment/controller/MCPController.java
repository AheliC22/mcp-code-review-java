
package com.mcp.assignment.controller;

import com.mcp.assignment.model.MCPRequest;
import com.mcp.assignment.model.MCPResponse;
import com.mcp.assignment.model.MCPTool;
import com.mcp.assignment.service.MCPService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MCPController {

    private final MCPService mcpService;

    public MCPController(MCPService mcpService) {
        this.mcpService = mcpService;
    }

    @PostMapping("/call")
    public ResponseEntity<MCPResponse> call(
            @RequestBody MCPRequest request) {
        return ResponseEntity.ok(mcpService.handleRequest(request));
    }

    @GetMapping("/tools")
    public ResponseEntity<List<MCPTool>> getTools() {
        return ResponseEntity.ok(mcpService.getTools());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("application", "mcp-code-review");
        return ResponseEntity.ok(status);
    }
}
