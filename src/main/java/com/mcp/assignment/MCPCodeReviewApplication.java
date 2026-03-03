package com.mcp.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application class for MCP Code Review Assistant
 * 
 * This Spring Boot application implements a Model Context Protocol (MCP) server
 * that provides tools for GitHub repository access and documentation search.
 */
@SpringBootApplication
public class MCPCodeReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(MCPCodeReviewApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("🚀 MCP Code Review Server Started!");
        System.out.println("===========================================");
        System.out.println("Server is ready to accept MCP requests.");
        System.out.println("Available tools:");
        System.out.println("  - get_repository: Get GitHub repository info");
        System.out.println("  - get_file_content: Read file from repository");
        System.out.println("  - search_docs: Search local documentation");
        System.out.println("===========================================\n");
    }
}

