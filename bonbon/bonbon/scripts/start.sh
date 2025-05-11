#!/usr/bin/env bash

# 프로젝트 루트 디렉토리 설정
PROJECT_ROOT="/home/ec2-user/app"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

# 로그 파일 경로
APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

# 현재 시간 저장
TIME_NOW=$(date '+%Y-%m-%d %H:%M:%S')

# build 파일 복사
echo "$TIME_NOW > JAR 파일 복사 시작" >> $DEPLOY_LOG
if [ -d "$PROJECT_ROOT/build/libs" ] && [ "$(ls -A $PROJECT_ROOT/build/libs)" ]; then
  cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE
  echo "$TIME_NOW > JAR 파일 복사 완료" >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 오류: 빌드 파일이 존재하지 않음" >> $DEPLOY_LOG
  exit 1
fi

# 기존 프로세스 종료 (있을 경우)
EXISTING_PID=$(pgrep -f "$JAR_FILE")
if [ -n "$EXISTING_PID" ]; then
  echo "$TIME_NOW > 기존 프로세스 종료 (PID: $EXISTING_PID)" >> $DEPLOY_LOG
  kill -9 $EXISTING_PID
fi

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행 시작" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG