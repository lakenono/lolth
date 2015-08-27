#!/bin/bash

# add class path ---------------------------------
CLASSPATH=".."

for jar in `ls ../lib`
do
	CLASSPATH="$CLASSPATH:../lib/$jar"
done

CLASSPATH="../classes:$CLASSPATH"

export CLASSPATH

# add java opts ----------------------------------

JAVA_OPTS="-Xms250m -Xmx250m -XX:MaxMetaspaceSize=50m"

#DEBUG="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=3306"

GC_LOG="-XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -Xloggc:../logs/gc.log"

JAVA_OPTS="$JAVA_OPTS $DEBUG $GC_LOG"

# echo info ----------------------------------
echo "CLASSPATH : $CLASSPATH"
echo "JAVA_OPTS: $JAVA_OPTS"

Fetch=`cat /app/bootstrap/fetch`

for p in `echo "$Fetch"`
do
	nohup java $JAVA_OPTS -Dapp.base=$PWD $p >> /dev/null 2>&1 &
done


