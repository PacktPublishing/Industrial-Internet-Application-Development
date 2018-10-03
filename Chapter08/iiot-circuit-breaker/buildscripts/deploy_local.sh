#!/bin/bash

#shutdown app
GTWPID=`ps ax | grep boot-gateway | grep -v grep | awk {'print $1'}`
if [ "x$GTWPID" = "x" ]; then
GTWPID=`ps -ef | grep boot-gateway | grep -v grep | cut -f3 -d' '`
fi
echo "Attempting to kill service PID ($GTWPID)"
kill -9 $GTWPID
echo "====== Shut down Complete ======"

if [ "$1" = "-k" ]; then
    exit 0
fi

#build
mvn clean install
if [ $? -ne 0 ]; then
    exit 1
fi
echo "====== Build complete ====="

#run app
export APP_VERSION=1.0.0.LOCAL_BUILD
export APP_PORT=8082
export APP_DEBUG_PORT=5008
java -jar -Dserver.port=${APP_PORT} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${APP_DEBUG_PORT} ./service/target/demo-spring-boot-gateway-${APP_VERSION}.jar &


# check if server started
while ! nc -z localhost $APP_PORT; do
  echo "=========>Waiting for SDS to start up..... " $APP_PORT
  sleep 5 # wait for 5 of the second before check again
done
echo "================All servers started===================="
