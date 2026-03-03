@echo off
echo ==========================================
echo Starting MCP Code Review Server
echo ==========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven not found! Please install Maven or use Gradle.
    pause
    exit /b 1
)

echo Building and starting the application...
echo.

mvn spring-boot:run

pause

