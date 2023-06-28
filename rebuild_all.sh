#!/bin/bash

echo Resource service...
cd resource_service
mvn -DskipTests clean package

echo Song service...
cd ..
cd song_service
mvn -DskipTests clean package

cd ..
echo OK.


