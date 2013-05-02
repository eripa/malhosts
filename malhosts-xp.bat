@echo off

for /f %%j in ("java.exe") do (
    set JAVA_HOME=%%~dp$PATH:j
)

if %JAVA_HOME%.==. (
   @echo Java software not found on your system. Please go to http://java.com to download a copy of Java.
) else (
    set basedir=%~dp0
    "%JAVA_HOME%\java.exe" -jar "%basedir%\malhosts.jar" %*
)

@echo on