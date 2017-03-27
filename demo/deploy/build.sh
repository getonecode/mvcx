#!/bin/sh
ROOT_UID=0

if [ "$UID" -eq "$ROOT_UID" ]
then
  echo "can not run with root."
  exit 1
fi


usage()
{
        echo "usage: `build.sh $0` pro|test|dev"
}
MAVEN_ENV=$1
if [ $# -ne 1 ]; then
        usage
        exit 1
fi
case $MAVEN_ENV in
        pro|test|dev) echo "maven evn is $MAVEN_ENV"
        ;;
        *)usage
        exit 1
        ;;
esac

CODE_HOME=/home/admin/code/bigshot
PACKAGE_COMMAND="mvn clean package -Dmaven.test.skip=true -P$MAVEN_ENV"
JAR_NAME=demo-1.0-SNAPSHOT.jar
config=config.json
APP_PNAME=demo-1.0
APP_PORT=6109
APP_CHECK_URL="http://localhost6091/index.htm"

echo "update code...."
cd $CODE_HOME
git pull
STATUS=$?
if [ "$STATUS" -ne "0" ]; then
        echo >&2 "git pull failed with $STATUS"
        exit 1
fi


echo "stop server...."
sleep 1

ps aux | grep -v grep | grep "$APP_PNAME" > /dev/null
STATUS=$?
if [ "$STATUS" -eq "0" ]; then
    echo "kill $APP_PNAME."
    ps ax | grep "$APP_PNAME" | grep -v grep | awk '{print $1}'| xargs kill
fi


cd ${CODE_HOME}
${INSTALL_COMMAND}


STATUS=$?
if [ "$STATUS" -eq "0" ]; then
        echo "maven deploy successful"
else
        echo >&2 "Deployment Failed"
        exit 1
fi


echo "start server...."

nohup java -jar ${JAR_NAME} -conf ${config} &

STATUS=$?
if [ "$STATUS" -eq "0" ]; then
        echo "server run successful"
else
        echo >&2 "server run failed"
        exit 1
fi

sleep 1

echo "start check app port $APP_PORT"
i=0
while :
do
  stillRunning=$(netstat -an|grep LISTEN|egrep "${APP_PORT}" |grep -v "grep")
  if [ "$stillRunning" ] ; then
    echo "port bind success.."
    break
  else
    ((i++));
    echo "wait port bind...${i}"
  fi
  sleep 1
done

echo "start check app url $APP_CHECK_URL"

i=0
while :
do
  httpstatus=$(curl  -m 10 -o /dev/null -s -w %{http_code} "${APP_CHECK_URL}")
  if [ "$httpstatus" -eq "200" ] ; then
    echo "server start success.."
    break
  else
    ((i++));
    echo "wait server start...${i} current return status ${httpstatus}"
  fi
  sleep 1
done
