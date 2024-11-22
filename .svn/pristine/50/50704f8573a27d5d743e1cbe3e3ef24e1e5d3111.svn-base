#!/bin/bash

JAR_AGENT_HOME=/APP/homedot_agent/bin
JAR_AGENT=${JAR_AGENT_HOME}/HomedotAgent.jar
JAVA_RUN=/APP/jdk1.8.0_51/bin/java

CLASSPATH=.:${JAR_AGENT_HOME}/lib/*:${JAR_AGENT}
RUN_COUNT=`ps -ef  | grep ${JAR_AGENT} | grep -v grep | grep -vc vi`
PID=`ps -eaf|grep homedot|grep -v grep|awk '{print $2}'`


if [ "$1" == "start" ] ; then

   ${JAVA_RUN} -cp ${CLASSPATH}:. -Dfile.encoding=UTF-8 apps.project.batch.TaskBatch &

elif [ "$1" == "stop" ] ; then

    kill -9 ${PID}

else
    echo "Usage: HomedotAgent.sh (command ...) "
    echo "commands :"
    echo "---------------------------------------------------"
    echo "  start       Start HomedotAgent "
    echo "  stop        Stop  HomedotAgent "
    echo "---------------------------------------------------"

fi