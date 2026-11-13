@echo off
title SkillSwap - SRM UNIVERSITY AP Server
echo ======================================================================
echo   SKILLSWAP - SRM UNIVERSITY AP COLLABORATIVE LEARNING PLATFORM
echo ======================================================================
echo.

if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-26.0.2" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.2"
        set "PATH=%JAVA_HOME%\bin;%PATH%"
    )
)

if not defined PORT set "PORT=8080"

echo Java Home: %JAVA_HOME%
echo.
echo Starting Embedded Tomcat Server at:
echo   --^> http://localhost:%PORT%/skillswap
echo.
echo Default Student Accounts:
echo   - aarav_sharma@srmap.edu.in  / Password@123
echo   - diya_reddy@srmap.edu.in    / Password@123
echo.
echo Default Administrator Account:
echo   - admin@srmap.edu.in         / Admin@123
echo.
echo Press Ctrl+C at any time to stop the server.
echo ======================================================================
echo.

mvn compile exec:java
pause
