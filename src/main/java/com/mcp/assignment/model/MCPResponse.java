package com.mcp.assignment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model Context Protocol Response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MCPResponse {
    
    @JsonProperty("jsonrpc")
    private String jsonrpc = "2.0";
    
    private Object result;
    
    private MCPError error;
    
    private String id;
    
    public static MCPResponse success(Object result, String id) {
        return MCPResponse.builder()
                .jsonrpc("2.0")
                .result(result)
                .id(id)
                .build();
    }
    
    public static MCPResponse error(int code, String message, String id) {
        return MCPResponse.builder()
                .jsonrpc("2.0")
                .error(new MCPError(code, message))
                .id(id)
                .build();
    }
}

