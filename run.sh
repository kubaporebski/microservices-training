#!/bin/bash
set -eu

BASE_DIRECTORY=$(pwd)
cd $BASE_DIRECTORY

# start docker containers
sudo docker-compose up -d

# wait a few seconds
echo Let\'s wait a few \(15\) seconds for docker containers to start...
sleep 15

# build and run song service
cd song_service

# kill the running instance of the Song Service
if [[ -f run.pid ]]; then
  echo Detected running Song service, killing it...
  kill `cat run.pid` && rm run.pid
fi

mvn -Dmaven.test.skip=true clean package
nohup java -jar target/song_service-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 & echo $! > run.pid
echo Song service should be running at http://localhost:33312/

cd $BASE_DIRECTORY

# build and run resource service
cd resource_service

# kill the running instance of the Resource Service
if [[ -f run.pid ]]; then
  echo Detected running Resource service, killing it...
  kill `cat run.pid` && rm run.pid
fi


mvn -Dmaven.test.skip=true clean package
nohup java -jar target/resource_service-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 & echo $! > run.pid
echo Resource service should be running at http://localhost:12333/
