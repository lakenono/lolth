#!/bin/bash

CLASSPATH="."
for jar in `ls ./lib`
do
	CLASSPATH="$CLASSPATH:./lib/$jar"
done

CLASSPATH="./classes:$CLASSPATH"

echo $CLASSPATH

export CLASSPATH

JAVA_OPTS='-Xmx50m -XX:+PrintGC -XX:+PrintGCTimeStamps -XX:+PrintGCDetails'

nohup java $JAVA_OPTS -Dapp.name=baidu.news -Dapp.base=$PWD lolth.tmall.comment.fetch.TmallGoodsCommentFetch >> ./logs/app.out 2>&1 &

tail -f logs/app.out