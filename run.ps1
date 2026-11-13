# SkillSwap SRM AP - PowerShell Startup Script
$Host.UI.RawUI.WindowTitle = "SkillSwap - SRM UNIVERSITY AP Server"

Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host "  SKILLSWAP - SRM UNIVERSITY AP COLLABORATIVE LEARNING PLATFORM" -ForegroundColor Cyan
Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host ""

if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Error "Java was not found. Install JDK 17 or newer and reopen PowerShell."
    exit 1
}
if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Error "Maven was not found. Install Apache Maven and reopen PowerShell."
    exit 1
}
$mysqlListening = Test-NetConnection -ComputerName localhost -Port 3306 -InformationLevel Quiet -WarningAction SilentlyContinue
if (-not $mysqlListening) {
    Write-Error "MySQL is not accepting connections on port 3306. Start the MySQL80 service, then run this script again."
    exit 1
}

Write-Host "Java and Maven found; MySQL is listening on port 3306." -ForegroundColor Green
Write-Host "Database settings are read from src/main/resources/db.properties." -ForegroundColor Gray
Write-Host "" 

if (-not $env:JAVA_HOME) {
    if (Test-Path "C:\Program Files\Java\jdk-26.0.2") {
        $env:JAVA_HOME = "C:\Program Files\Java\jdk-26.0.2"
        $env:Path = "$env:JAVA_HOME\bin;$env:Path"
    }
}

$portDisplay = if ($env:PORT) { $env:PORT } else { "8080" }

Write-Host "Java Home: $env:JAVA_HOME" -ForegroundColor Gray
Write-Host ""
Write-Host "Starting Embedded Tomcat Server at:" -ForegroundColor Green
Write-Host "  --> Default: http://localhost:$portDisplay/skillswap" -ForegroundColor Yellow
Write-Host "       If that port is occupied, the launcher will select the next free port and print its URL." -ForegroundColor Gray
Write-Host ""
Write-Host "Default Student Accounts:" -ForegroundColor White
Write-Host "  - aarav_sharma@srmap.edu.in  / Password@123" -ForegroundColor Gray
Write-Host "  - diya_reddy@srmap.edu.in    / Password@123" -ForegroundColor Gray
Write-Host ""
Write-Host "Default Administrator Account:" -ForegroundColor White
Write-Host "  - admin@srmap.edu.in         / Admin@123" -ForegroundColor Gray
Write-Host ""
Write-Host "Press Ctrl+C at any time to stop the server." -ForegroundColor Yellow
Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host ""

mvn compile exec:java
if ($LASTEXITCODE -ne 0) {
    Write-Error "SkillSwap did not start. Review the Maven error above."
    exit $LASTEXITCODE
}
