# Java MCP Code Review Assistant

## Overview

A Java-based Code Review Assistant built using Spring Boot. It exposes GitHub repository operations and local Markdown documentation search through JSON-RPC-style REST endpoints.

## Features

- Retrieve information about a configured GitHub repository.
- Read file contents from the configured GitHub repository.
- Search local Markdown documentation using keywords.
- Expose available tools through a tools-list endpoint.
- Handle invalid requests, unknown methods, missing arguments, and tool execution errors.

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- Maven
- Spring Web
- Spring WebFlux
- Jackson
- JUnit / Spring Boot Test

## Project Structure

```text
MCPAssignment-Java/
├── docs/
├── src/
│   ├── main/
│   │   ├── java/com/mcp/assignment/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── service/
│   │   │   ├── client/
│   │   │   └── McpCodeReviewApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## Prerequisites

- Java 17 or later
- Maven
- GitHub Personal Access Token
- Access to the GitHub repository being inspected

## Configuration

Set the following environment variables before starting the application:

```bash
export GITHUB_TOKEN="your_github_token"
export GITHUB_REPO_OWNER="your_github_username"
export GITHUB_REPO_NAME="your_repository_name"
```

Optional configuration:

```bash
export SERVER_PORT=8080
export GITHUB_API_BASE_URL="https://api.github.com"
```

Never commit your actual GitHub token to source control.

## Build and Run

Build and test:

```bash
mvn clean test
```

Start the application:

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## Available Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/health` | Application health |
| GET | `/tools` | List available tools |
| POST | `/call` | Process JSON-RPC-style requests |

## Available Tools

### 1. get_repository

Retrieves information about the configured GitHub repository.

Arguments: None.

### 2. get_file_content

Reads a file from the configured GitHub repository.

Required argument:

```json
{
  "path": "README.md"
}
```

### 3. search_docs

Searches local Markdown documentation using a keyword.

Required argument:

```json
{
  "keyword": "GitHub"
}
```

## Example Requests

List tools:

```bash
curl http://localhost:8080/tools
```

Call a tool:

```bash
curl -X POST http://localhost:8080/call \
-H "Content-Type: application/json" \
-d '{"jsonrpc":"2.0","method":"tools/call","params":{"name":"get_repository","arguments":{}},"id":1}'
```

Check application health:

```bash
curl http://localhost:8080/health
```

## Testing

Run the automated tests:

```bash
mvn clean test
```

The API can also be tested manually using curl. Verify repository retrieval, file retrieval, documentation search, tool listing, health status, and invalid tool arguments.

## Error Handling

The application returns JSON-RPC-style errors for invalid requests, unsupported methods, invalid parameters, and tool execution failures.

## Notes

This project currently exposes custom JSON-RPC-style REST endpoints. It should not be considered a complete implementation of the official MCP transport and initialization lifecycle unless those features are added separately.
