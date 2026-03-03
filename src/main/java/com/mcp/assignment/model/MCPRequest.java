package com.mcp.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

/**
 * Model Context Protocol Request
 */
@Data
public class MCPRequest {
    
    @JsonProperty("jsonrpc")
    private String jsonrpc = "2.0";
    
    private String method;
    
    private Map<String, Object> params;
    
    private String id;
}

