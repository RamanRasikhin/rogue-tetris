@echo off
setlocal
cd /d "%~dp0"
if not exist "logs" mkdir "logs"
set "JAVA_EXE="
if exist "%~dp0runtime\bin\java.exe" set "JAVA_EXE=%~dp0runtime\bin\java.exe"
if not defined JAVA_EXE if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
if not defined JAVA_EXE if exist "%ProgramFiles%\Java\jdk-17\bin\java.exe" set "JAVA_EXE=%ProgramFiles%\Java\jdk-17\bin\java.exe"
if not defined JAVA_EXE for /f "delims=" %%J in ('where java 2^>nul') do if not defined JAVA_EXE set "JAVA_EXE=%%J"
if not defined JAVA_EXE (
  echo Java 17 or newer is required for the source/JAR version.
  echo For a double-click launch without Java installation, download the Windows ZIP from Releases.
  pause
  exit /b 1
)
set "APP_JAR=%~dp0rogue-tetris-1.0.1.jar"
if not exist "%APP_JAR%" set "APP_JAR=%~dp0lwjgl3\build\libs\rogue-tetris-1.0.1.jar"
if not exist "%APP_JAR%" (
  echo Building RogueTetris from source. The first build needs a JDK 17+ and internet access.
  for %%J in ("%JAVA_EXE%") do set "JAVA_HOME=%%~dpJ.."
  call "%~dp0gradlew.bat" lwjgl3:dist > "%~dp0logs\build.log" 2>&1
  if errorlevel 1 (
    echo Build failed. See logs\build.log for details. Make sure a JDK 17+ is installed.
    type "%~dp0logs\build.log"
    pause
    exit /b 1
  )
)
echo Starting RogueTetris...
"%JAVA_EXE%" -jar "%APP_JAR%" > "%~dp0logs\launch.log" 2>&1
if errorlevel 1 (
  echo The game could not start. Details are in logs\launch.log.
  type "%~dp0logs\launch.log"
  pause
  exit /b 1
)
exit /b 0
