#!/bin/bash
set -eu
_PATH=`pwd`

echo Eureka Server...
cd "$_PATH/eureka_service"
mvn -DskipTests clean package

echo Resource service...
cd "$_PATH/resource_service"
mvn -DskipTests clean package

echo Song service...
cd "$_PATH/song_service"
mvn -DskipTests clean package

cd "$_PATH"
echo OK.


