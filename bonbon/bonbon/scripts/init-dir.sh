#!/bin/bash

# 프로젝트 루트 디렉토리 경로
PROJECT_ROOT="/home/ec2-user/app"

# 디렉토리 확인 후 생성
if [ ! -d "$PROJECT_ROOT" ]; then
  echo "$(date '+%Y-%m-%d %H:%M:%S') > $PROJECT_ROOT 디렉토리 없음. 생성 중..." >> /home/ec2-user/deploy.log
  mkdir -p $PROJECT_ROOT
  echo "$(date '+%Y-%m-%d %H:%M:%S') > $PROJECT_ROOT 디렉토리 생성 완료." >> /home/ec2-user/deploy.log
else
  echo "$(date '+%Y-%m-%d %H:%M:%S') > $PROJECT_ROOT 디렉토리가 이미 존재합니다." >> /home/ec2-user/deploy.log
fi

# 디렉토리 권한 설정 (ec2-user에게 소유권 부여)
echo "$(date '+%Y-%m-%d %H:%M:%S') > $PROJECT_ROOT 디렉토리 권한 변경 중..." >> /home/ec2-user/deploy.log
chown -R ec2-user:ec2-user $PROJECT_ROOT
chmod -R 755 $PROJECT_ROOT
echo "$(date '+%Y-%m-%d %H:%M:%S') > $PROJECT_ROOT 디렉토리 권한 설정 완료." >> /home/ec2-user/deploy.log
