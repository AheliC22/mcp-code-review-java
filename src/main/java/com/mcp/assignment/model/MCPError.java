package com.mcp.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MCP Error object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MCPError {
    
    private int code;
    
    private String message;
}

