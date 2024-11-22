@echo off


rem FIXME path

set "JAR_AGENT_HOME=D:/intellij_workspace/rserp_agent/20240426_rserp_agent/bin"
set "JAR_AGENT=%JAR_AGENT_HOME%/zeonsRserpAgent.jar"
set "JAVA_RUN="C:/Program Files/java/java-1.8.0-openjdk-1.8.0.332-1.b09.ojdkbuild.windows.x86_64/bin/java""


set "CLASSPATH=.;%JAR_AGENT_HOME%/lib/*;%JAR_AGENT%"

if "%1" == "" goto start

if "%1" == "stop" goto stop

if "%1" == "help" goto help
goto end

:start
%JAVA_RUN% -cp "%CLASSPATH%" apps.project.batch.TaskBatch
goto end

:stop
TASKKILL /FI "WINDOWTITLE eq %JAR_AGENT%"
goto end

:help
echo Usage: agent.bat (command ...) 
echo commands :
echo ---------------------------------------------------
echo   start       Start Agent 
echo   stop        Stop  Agent 
echo ---------------------------------------------------
goto end


:end
