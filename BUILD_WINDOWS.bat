@echo off
setlocal
cd /d "%~dp0"
where java >nul 2>nul || (echo [ERROR] Java 21 JDK required. & pause & exit /b 1)
where gradle >nul 2>nul || (echo [ERROR] Gradle 8.x is required. Install Gradle or add a Gradle wrapper. & pause & exit /b 1)
call gradle clean build --stacktrace
if errorlevel 1 (echo [ERROR] Build failed. See messages above. & pause & exit /b 1)
echo [OK] Check build\libs for the mod JAR.
pause
