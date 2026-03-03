# 🔧 Simple Code Review Assistant - MCP Assignment (Java/Spring Boot)

**Assignment Duration:** 6-8 Hours  
**Target Audience:** Intermediate Level Java Developers  
**Focus:** Model Context Protocol (MCP) Basics with Spring Boot

## 🎯 Learning Objectives

By completing this assignment, you will:
- Build a basic MCP server using **Model Context Protocol** with Spring Boot
- Implement simple tool interfaces for AI models
- Practice GitHub API integration using WebClient
- Understand how AI models connect to external data sources
- Work with REST APIs in Spring Boot

## 📋 Prerequisites

- Java 17+ experience
- Spring Boot basics
- Basic understanding of REST APIs
- Familiarity with GitHub API
- Maven or Gradle knowledge
- Basic knowledge of AI/LLM concepts

## 🚀 Quick Start

### 1. Environment Setup

#### Using Maven:
```bash
# Clone and navigate to project
cd MCPAssignment-Java

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

#### Using Gradle:
```bash
# Clone and navigate to project
cd MCPAssignment-Java

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

### 2. Configuration

Create `.env` file in the project root or set environment variables:
```env
GITHUB_TOKEN=your_github_token_here
GITHUB_REPO_OWNER=your_github_username
GITHUB_REPO_NAME=your_repository_name
SERVER_PORT=8080
```

**Windows PowerShell:**
```powershell
$env:GITHUB_TOKEN="your_token"
$env:GITHUB_REPO_OWNER="your_username"
$env:GITHUB_REPO_NAME="your_repo"
```

**Linux/Mac:**
```bash
export GITHUB_TOKEN="your_token"
export GITHUB_REPO_OWNER="your_username"
export GITHUB_REPO_NAME="your_repo"
```

### 3. Run the MCP Server

```bash
# Using Maven
mvn spring-boot:run

# Using Gradle
./gradlew bootRun

# Or run the compiled JAR
java -jar target/mcp-code-review-1.0.0.jar
```

### 4. Test the Server

#### Option 1: Using the Java Client
```bash
# Compile and run the client
mvn exec:java -Dexec.mainClass="com.mcp.assignment.client.MCPClient"
```

#### Option 2: Using cURL
```bash
# Health check
curl http://localhost:8080/mcp/health

# List tools
curl http://localhost:8080/mcp/tools

# Call a tool
curl -X POST http://localhost:8080/mcp/call \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "tools/call",
    "params": {
      "name": "get_repository",
      "arguments": {
        "owner": "spring-projects",
        "repo": "spring-boot"
      }
    },
    "id": "1"
  }'
```

## 📁 Project Structure

```
MCPAssignment-Java/
├── README.md                           # This file
├── pom.xml                             # Maven dependencies
├── build.gradle                        # Gradle dependencies (alternative)
├── env.example                         # Environment template
├── .gitignore                          # Git ignore rules
├── docs/                               # Documentation files
│   ├── api_guide.md                    # API documentation
│   └── setup_guide.md                  # Setup instructions
└── src/
    └── main/
        ├── java/com/mcp/assignment/
        │   ├── MCPCodeReviewApplication.java  # Main application
        │   ├── client/
        │   │   └── MCPClient.java             # Test client
        │   ├── config/
        │   │   ├── GitHubConfig.java          # GitHub configuration
        │   │   ├── DocsConfig.java            # Docs configuration
        │   │   └── WebClientConfig.java       # HTTP client config
        │   ├── controller/
        │   │   └── MCPController.java         # REST endpoints
        │   ├── model/
        │   │   ├── MCPRequest.java            # MCP request model
        │   │   ├── MCPResponse.java           # MCP response model
        │   │   ├── MCPError.java              # MCP error model
        │   │   ├── MCPTool.java               # Tool definition
        │   │   ├── RepositoryInfo.java        # GitHub repo model
        │   │   ├── FileContent.java           # GitHub file model
        │   │   └── DocumentSearchResult.java  # Search result model
        │   └── service/
        │       ├── MCPService.java            # Main MCP service
        │       ├── GitHubService.java         # GitHub integration
        │       └── DocumentationService.java  # Doc search service
        └── resources/
            └── application.properties         # Application config
```

## 🏗️ System Architecture

### MCP Tools

| Tool | Purpose | Implementation |
|------|---------|---------------|
| **get_repository** | Get repo info from GitHub | GitHub REST API via WebClient |
| **search_docs** | Search local documentation | File system search with Java NIO |
| **get_file_content** | Read file from repo | GitHub API with Base64 decoding |

### Architecture Flow
```
Client → Spring Boot REST Controller → MCP Service → Tool Services → External APIs/Files → Response
```

### Technology Stack
- **Spring Boot 3.2.0** - Application framework
- **Spring WebFlux** - Reactive HTTP client (WebClient)
- **Jackson** - JSON processing
- **Lombok** - Reduce boilerplate code
- **Java 17** - Programming language

## 📋 Core Requirements

### Must-Have Features (6-8 hours scope)

1. **Basic MCP Server**
   - Spring Boot application with REST endpoints
   - Support 3 simple tools (listed above)
   - Follow basic MCP protocol (JSON-RPC 2.0)
   - Handle errors gracefully with proper status codes

2. **GitHub Integration**
   - Connect to GitHub API using token authentication
   - Implement `get_repository` tool
   - Implement `get_file_content` tool with Base64 decoding
   - Add basic error handling and timeouts

3. **Documentation Search**
   - Implement `search_docs` tool for local files
   - Search through markdown files in `/docs` folder
   - Return relevant file content
   - Support simple keyword matching (case-insensitive)

4. **Simple Client**
   - Create `MCPClient.java` to test your MCP server
   - Demonstrate all 3 tools working
   - Show real GitHub data retrieval
   - Display search results in console

## 🔧 Implementation Details

### Spring Boot Features Used

1. **Configuration Management**
   - `@ConfigurationProperties` for type-safe configuration
   - Environment variable binding
   - `application.properties` for defaults

2. **Dependency Injection**
   - Constructor injection with Lombok's `@RequiredArgsConstructor`
   - Service layer pattern
   - Bean configuration

3. **REST Controllers**
   - `@RestController` for MCP endpoints
   - `@RequestBody` for JSON request handling
   - `ResponseEntity` for proper HTTP responses

4. **WebClient (Reactive)**
   - Non-blocking HTTP client for GitHub API
   - Header configuration for authentication
   - Timeout handling

5. **Error Handling**
   - Try-catch blocks with proper error responses
   - MCP error format (JSON-RPC 2.0)
   - Logging with SLF4J

## 🔧 Development Commands

```bash
# Build the project
mvn clean package
# or
./gradlew clean build

# Run tests
mvn test
# or
./gradlew test

# Run the application
mvn spring-boot:run
# or
./gradlew bootRun

# Run the client (in another terminal)
mvn exec:java -Dexec.mainClass="com.mcp.assignment.client.MCPClient"

# Check application health
curl http://localhost:8080/mcp/health

# View application logs
# Logs are printed to console by default
```

## 📝 API Endpoints

### 1. Health Check
```http
GET /mcp/health
```

### 2. List Tools
```http
GET /mcp/tools
```

Response:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "tools": [
      {
        "name": "get_repository",
        "description": "Get information about a GitHub repository",
        "inputSchema": {...}
      },
      ...
    ]
  }
}
```

### 3. Call Tool
```http
POST /mcp/call
Content-Type: application/json

{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "params": {
    "name": "get_repository",
    "arguments": {
      "owner": "spring-projects",
      "repo": "spring-boot"
    }
  },
  "id": "1"
}
```

## 🧪 Testing

### Unit Tests (Exercise Verification)

The project includes unit tests that verify your implementation of the service-layer exercises. So that **`mvn clean install` succeeds** before you implement them, the 6 exercise-verification tests are excluded by default. After you implement:

- **GitHubService** (Step 4 in IMPLEMENTATION_GUIDE): `getRepository()` and `getFileContent()` with GitHub API calls and Base64 decoding
- **DocumentationService** (Step 5): `searchDocumentation()` with file walking, `.md` filtering, case-insensitive search, and sorting by match count

run **all** tests (including the exercise tests). When your implementation is correct, all 12 tests should pass:

```bash
# Run all tests including exercise verification
mvn test -Pfull-tests
```

- **Default** `mvn test` or `mvn clean install`: runs 6 tests; exercise tests are skipped.
- **Full verification** `mvn test -Pfull-tests`: runs all 12 tests; use this to confirm your implementation.

Tests are in `src/test/java/com/mcp/assignment/service/`:
- `GitHubServiceTest` – getRepository, getFileContent, error handling (tagged `exercise`)
- `DocumentationServiceTest` – search with temp docs, case-insensitivity, sort order, missing directory (tagged `exercise`)
- `MCPServiceTest` – tool listing and routing

### Manual Testing Steps

1. **Start the server:**
   ```bash
   mvn spring-boot:run
   ```

2. **Wait for startup:**
   ```
   🚀 MCP Code Review Server Started!
   ```

3. **Run the test client:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.mcp.assignment.client.MCPClient"
   ```

4. **Verify output:**
   - Health check passes
   - Tools are listed
   - Repository info retrieved
   - File content retrieved
   - Documentation search works

## 📝 Submission Requirements

### Required Files
- ✅ Complete Spring Boot application
- ✅ `pom.xml` or `build.gradle` with dependencies
- ✅ `MCPClient.java` - Test client
- ✅ `env.example` - Environment template
- ✅ Sample docs in `/docs` folder
- ✅ `README.md` - This documentation

### Demo Requirements
- ✅ Show MCP server starting up
- ✅ Demonstrate GitHub repository access
- ✅ Show documentation search working
- ✅ Explain your implementation approach
- ✅ Show error handling

## 🐛 Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```
   Change SERVER_PORT in .env or application.properties
   ```

2. **GitHub API Authentication Failed**
   ```
   Verify GITHUB_TOKEN is set correctly
   Check token has necessary permissions
   ```

3. **Documentation Not Found**
   ```
   Ensure docs/ folder exists in project root
   Check file paths in application.properties
   ```

4. **Build Failures**
   ```
   # Clean and rebuild
   mvn clean install -U
   # or
   ./gradlew clean build --refresh-dependencies
   ```

## 🔗 Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring WebClient Guide](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)
- [MCP Python SDK](https://github.com/modelcontextprotocol/python-sdk)
- [GitHub REST API Documentation](https://docs.github.com/en/rest)
- [MCP Examples](https://modelcontextprotocol.io/docs/concepts/tools)
- [JSON-RPC 2.0 Specification](https://www.jsonrpc.org/specification)

## 📚 Learning Resources

### Spring Boot
- [Spring Boot Getting Started](https://spring.io/guides/gs/spring-boot/)
- [Building REST Services](https://spring.io/guides/tutorials/rest/)
- [Configuration Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties)

### Java Features
- [Java 17 Features](https://www.oracle.com/java/technologies/javase/17-relnote-issues.html)
- [Records and Pattern Matching](https://openjdk.org/jeps/395)

## 🎓 Next Steps

After completing this assignment, consider:

1. **Add More Tools**
   - List repository files
   - Search code in repository
   - Get commit history

2. **Enhance Error Handling**
   - Custom exception classes
   - Global exception handler
   - Better error messages

3. **Add Caching**
   - Spring Cache abstraction
   - Cache GitHub API responses
   - Implement rate limiting

4. **Add Tests**
   - Unit tests with JUnit 5
   - Integration tests with Spring Boot Test
   - Mock GitHub API with WireMock

5. **Improve Documentation**
   - Add Swagger/OpenAPI
   - Create Postman collection
   - Add sequence diagrams

## 📄 License

This project is for educational purposes as part of the MCP Assignment.

## 👥 Support

For questions or issues:
- Review the troubleshooting section
- Check Spring Boot documentation
- Review MCP protocol specification
- Examine the sample code and comments

---

**Happy Coding! 🚀**

