#!/bin/sh
BASEDIR=$(cd "$(dirname "$0")"; pwd)
JAVA_EXEC=${JAVA_HOME:-java}
exec "$JAVA_EXEC" -Dmaven.multiModuleProjectDirectory="$BASEDIR" -classpath "$BASEDIR/.mvn/wrapper/maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain "$@"
