@echo off
echo ==========================================
echo Running MCP Test Client
echo ==========================================
echo.
echo Make sure the server is running first!
echo.

timeout /t 2 >nul

mvn exec:java -Dexec.mainClass="com.mcp.assignment.client.MCPClient"

pause

