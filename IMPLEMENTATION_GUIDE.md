# Implementation Guide - MCP Assignment (Java/Spring Boot)

## Overview
This guide walks you through the implementation of a Model Context Protocol (MCP) server using Java and Spring Boot.

## Step-by-Step Implementation

### Step 1: Project Setup (30 minutes)

1. **Create Spring Boot Project Structure**
   ```bash
   # Using Maven
   mvn archetype:generate -DgroupId=com.mcp.assignment \
       -DartifactId=mcp-code-review -DarchetypeArtifactId=maven-archetype-quickstart
   ```

2. **Add Dependencies to pom.xml**
   - Spring Boot Starter Web
   - Spring Boot Starter WebFlux (for WebClient)
   - Lombok
   - Jackson

3. **Configure application.properties**
   - Set server port
   - Add GitHub configuration properties
   - Configure logging

### Step 2: Create Model Classes (45 minutes)

1. **MCP Protocol Models**
   - `MCPRequest.java` - JSON-RPC 2.0 request format
   - `MCPResponse.java` - JSON-RPC 2.0 response format
   - `MCPError.java` - Error object
   - `MCPTool.java` - Tool definition

2. **Domain Models**
   - `RepositoryInfo.java` - GitHub repository data
   - `FileContent.java` - GitHub file data
   - `DocumentSearchResult.java` - Search results

**Key Points:**
- Use `@Data` from Lombok to reduce boilerplate
- Use `@JsonProperty` for GitHub API field mapping
- Use `@JsonIgnoreProperties(ignoreUnknown = true)` for flexible parsing

### Step 3: Configuration Classes (30 minutes)

1. **Create GitHubConfig.java**
   ```java
   @Configuration
   @ConfigurationProperties(prefix = "github")
   public class GitHubConfig {
       private String token;
       private Repo repo;
       private Api api;
       // ... nested classes
   }
   ```

2. **Create WebClientConfig.java**
   - Configure WebClient.Builder bean
   - Will be used for HTTP requests

3. **Create DocsConfig.java**
   - Configure documentation path

**Key Points:**
- Use `@ConfigurationProperties` for type-safe configuration
- Bind to environment variables automatically
- Provide sensible defaults

### Step 4: GitHub Integration Service (90 minutes)

1. **Create GitHubService.java**

2. **Implement getRepository() method**
   ```java
   public Map<String, Object> getRepository(String owner, String repo) {
       // Build GitHub API URL
       // Create WebClient with auth headers
       // Make GET request
       // Parse response to RepositoryInfo
       // Return result map
   }
   ```

3. **Implement getFileContent() method**
   ```java
   public Map<String, Object> getFileContent(String owner, String repo, String path) {
       // Build GitHub API URL for file content
       // Make GET request
       // Decode Base64 content
       // Return decoded content
   }
   ```

**Key Points:**
- Use WebClient for non-blocking HTTP requests
- Add Authorization header with GitHub token
- Handle timeouts (30 seconds recommended)
- Decode Base64 content from GitHub API
- Wrap responses in success/error format

### Step 5: Documentation Search Service (60 minutes)

1. **Create DocumentationService.java**

2. **Implement searchDocumentation() method**
   ```java
   public Map<String, Object> searchDocumentation(String query) {
       // Get docs path from config
       // Walk file tree using Files.walk()
       // Filter for .md files
       // Search each file for query
       // Count matches
       // Sort by relevance
       // Return results
   }
   ```

3. **Helper methods**
   - `searchFile()` - Search single file
   - `countOccurrences()` - Count search term occurrences

**Key Points:**
- Use Java NIO for file operations
- Case-insensitive search
- Return full content if matches found
- Sort results by match count

### Step 6: MCP Service (Main Logic) (60 minutes)

1. **Create MCPService.java**

2. **Implement handleRequest() method**
   ```java
   public MCPResponse handleRequest(MCPRequest request) {
       // Route based on method
       // Call appropriate handler
       // Return MCP response
   }
   ```

3. **Implement getTools() method**
   - Define all available tools
   - Include name, description, and input schema
   - Use JSON Schema format

4. **Implement callTool() method**
   ```java
   private Object callTool(Map<String, Object> params) {
       // Extract tool name and arguments
       // Route to appropriate service
       // Return result
   }
   ```

**Key Points:**
- Use switch expressions (Java 17 feature)
- Follow JSON-RPC 2.0 specification
- Define clear input schemas for tools
- Handle unknown methods gracefully

### Step 7: REST Controller (45 minutes)

1. **Create MCPController.java**

2. **Implement endpoints**
   ```java
   @PostMapping("/call")
   public ResponseEntity<MCPResponse> handleMCPCall(@RequestBody MCPRequest request)
   
   @GetMapping("/tools")
   public ResponseEntity<MCPResponse> listTools()
   
   @GetMapping("/health")
   public ResponseEntity<String> health()
   ```

**Key Points:**
- Use `@RestController` annotation
- Accept JSON request bodies
- Return proper HTTP status codes
- Add logging for debugging

### Step 8: Main Application (15 minutes)

1. **Create MCPCodeReviewApplication.java**
   ```java
   @SpringBootApplication
   public class MCPCodeReviewApplication {
       public static void main(String[] args) {
           SpringApplication.run(MCPCodeReviewApplication.class, args);
           // Print startup message
       }
   }
   ```

### Step 9: Test Client (60 minutes)

1. **Create MCPClient.java**

2. **Implement test methods**
   - `testHealthCheck()`
   - `testListTools()`
   - `testGetRepository()`
   - `testGetFileContent()`
   - `testSearchDocs()`

3. **Helper method**
   ```java
   private MCPResponse callTool(String toolName, Map<String, Object> arguments) {
       // Build MCP request
       // Send HTTP POST
       // Parse response
   }
   ```

**Key Points:**
- Use Java HttpClient (built-in since Java 11)
- Test all tools systematically
- Pretty-print JSON responses
- Add descriptive output

### Step 10: Documentation (45 minutes)

1. **Create sample documentation files**
   - `docs/api_guide.md`
   - `docs/setup_guide.md`

2. **Write comprehensive README.md**
   - Project overview
   - Setup instructions
   - Usage examples
   - API documentation
   - Troubleshooting

3. **Create helper scripts**
   - `run-server.sh` / `run-server.bat`
   - `run-client.sh` / `run-client.bat`

### Step 11: Testing & Refinement (60 minutes)

1. **Test all endpoints**
   - Start server
   - Run client
   - Verify all tools work

2. **Test error cases**
   - Invalid GitHub token
   - Non-existent repository
   - Missing files
   - Empty documentation

3. **Refine error messages**
   - Make them user-friendly
   - Include helpful context

4. **Check logging**
   - Ensure proper log levels
   - Add debugging information

## Time Breakdown

| Task | Estimated Time |
|------|----------------|
| Project Setup | 30 min |
| Model Classes | 45 min |
| Configuration | 30 min |
| GitHub Service | 90 min |
| Documentation Service | 60 min |
| MCP Service | 60 min |
| REST Controller | 45 min |
| Main Application | 15 min |
| Test Client | 60 min |
| Documentation | 45 min |
| Testing & Refinement | 60 min |
| **Total** | **540 min (9 hours)** |

## Tips for Success

### Java/Spring Boot Best Practices

1. **Use Dependency Injection**
   - Constructor injection preferred
   - Use `@RequiredArgsConstructor` from Lombok

2. **Configuration Management**
   - Use `@ConfigurationProperties` over `@Value`
   - Provide defaults in `application.properties`

3. **Error Handling**
   - Use try-catch blocks
   - Return meaningful error messages
   - Log errors appropriately

4. **Code Organization**
   - Separate concerns (controller, service, model)
   - Use meaningful package names
   - Keep classes focused and single-purpose

### MCP Implementation Tips

1. **Follow JSON-RPC 2.0**
   - Include `jsonrpc: "2.0"` in all messages
   - Use proper error codes
   - Include request `id` in responses

2. **Tool Schema Definition**
   - Use JSON Schema format
   - Mark required parameters
   - Provide clear descriptions

3. **Response Format**
   - Consistent success/error structure
   - Include helpful metadata
   - Pretty-print for debugging

### GitHub API Tips

1. **Authentication**
   - Use Personal Access Token
   - Add to Authorization header
   - Test token permissions

2. **Rate Limiting**
   - GitHub limits: 5000 req/hour with token
   - Add timeout handling
   - Consider caching

3. **Content Encoding**
   - File content is Base64 encoded
   - Remember to decode
   - Handle encoding properly

## Common Pitfalls to Avoid

1. **Forgetting Base64 Decoding**
   - GitHub returns file content in Base64
   - Must decode before returning

2. **Not Handling Timeouts**
   - GitHub API can be slow
   - Set reasonable timeouts

3. **Hardcoding Configuration**
   - Use environment variables
   - Don't commit tokens

4. **Poor Error Messages**
   - Be specific about what went wrong
   - Include context for debugging

5. **Not Testing Error Cases**
   - Test with invalid inputs
   - Handle missing files/repos

## Validation Checklist

Before submitting, verify:

- [ ] Server starts without errors
- [ ] Health endpoint responds
- [ ] Tools list endpoint works
- [ ] get_repository tool retrieves data
- [ ] get_file_content tool decodes content
- [ ] search_docs finds documentation
- [ ] Client tests all tools successfully
- [ ] Error handling works properly
- [ ] Configuration via environment variables
- [ ] README is comprehensive
- [ ] Code is well-commented
- [ ] No hardcoded secrets
- [ ] Documentation files present

## Next Steps

After completing the basic implementation:

1. Add unit tests with JUnit 5
2. Add integration tests
3. Implement caching
4. Add more GitHub tools
5. Create Swagger documentation
6. Deploy to cloud platform

## Resources

- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [WebClient Documentation](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)
- [GitHub API v3](https://docs.github.com/en/rest)
- [JSON-RPC 2.0 Spec](https://www.jsonrpc.org/specification)
- [Java 17 Features](https://openjdk.org/projects/jdk/17/)

---

Good luck with your implementation! 🚀

