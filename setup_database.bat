@echo off
echo ========================================================
echo   SkillSwap SRM AP - Database Setup (MySQL)
echo ========================================================
echo.
set /p MYSQL_USER=Enter MySQL username (default: root): 
if "%MYSQL_USER%"=="" set MYSQL_USER=root

where mysql >nul 2>nul
if errorlevel 1 (
    echo [ERROR] mysql.exe was not found. Add the MySQL Server bin folder to PATH.
    echo Example: C:\Program Files\MySQL\MySQL Server 8.0\bin
    pause
    exit /b 1
)

echo Importing database/skillswap.sql into MySQL...
mysql --user="%MYSQL_USER%" --password < database\skillswap.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] Database 'skillswap_db' created and seed data imported!
    echo Database created and demo data imported.
) else (
    echo.
    echo [ERROR] Failed to import database. Check your MySQL password or service status.
)
pause

