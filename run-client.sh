#!/bin/bash

echo "=========================================="
echo "Running MCP Test Client"
echo "=========================================="
echo ""
echo "Make sure the server is running first!"
echo ""

sleep 2

mvn exec:java -Dexec.mainClass="com.mcp.assignment.client.MCPClient"

