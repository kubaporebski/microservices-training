#!/bin/bash
set -eu

BASE_DIRECTORY=$(pwd)/..
cd $BASE_DIRECTORY

# start docker containers
sudo docker-compose up -d

# build and run song service
cd song_service
mvn -Dmaven.test.skip=true clean package
nohup java -jar target/song_service-0.0.1-SNAPSHOT.jar &

cd $BASE_DIRECTORY

# build and run resource service
cd resource_service
mvn -Dmaven.test.skip=true clean package
nohup java -jar target/resource_service-0.0.1-SNAPSHOT.jar &
