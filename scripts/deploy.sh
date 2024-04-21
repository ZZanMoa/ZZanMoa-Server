#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/zzanmoa
cd $REPOSITORY

APP_NAME=zzanmoa
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z "$CURRENT_PID" ]
then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  # 최대 10초 동안 대기
  TIMEOUT=10
  while kill -0 $CURRENT_PID 2> /dev/null; do
    sleep 1
    let TIMEOUT-=1
    if [ $TIMEOUT -le 0 ]; then
      echo "프로세스 종료 시간 초과, 강제 종료합니다."
      kill -9 $CURRENT_PID
      break
    fi
  done
fi

echo "> $JAR_NAME에 실행권한 추가"
chmod +x $JAR_NAME

echo "> Deploy - $JAR_PATH "
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
