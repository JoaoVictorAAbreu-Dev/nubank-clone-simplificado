@ECHO OFF
SETLOCAL
SET MAVEN_PROJECTBASEDIR=%~dp0
IF NOT "%MAVEN_BASEDIR%"=="" SET MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
SET WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
SET WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties
SET JAVA_EXEC=%JAVA_HOME%\bin\java.exe
IF EXIST "%JAVA_EXEC%" GOTO execute
SET JAVA_EXEC=java
:execute
"%JAVA_EXEC%" -Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR% -classpath %WRAPPER_JAR% org.apache.maven.wrapper.MavenWrapperMain %*
ENDLOCAL
