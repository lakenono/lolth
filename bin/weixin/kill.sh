#!/bin/bash

className='lolth.weixin.sogou.WeiXinListFetch'
threadId=`ps aux | grep $className | grep -v grep | grep -v retomcat | awk '{print $2}'`

echo "kill $className begin"
kill $threadId

ps -ef|grep $className