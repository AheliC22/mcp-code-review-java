#!/bin/bash

echo "=========================================="
echo "Starting MCP Code Review Server"
echo "=========================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven not found! Please install Maven or use Gradle."
    exit 1
fi

echo "Building and starting the application..."
echo ""

mvn spring-boot:run

