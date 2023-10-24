#!/bin/bash
set -eu

echo Common Tools Library project
mvn -f jpcommons clean install

echo Eureka Server...
mvn -f eureka_service/ -DskipTests clean package

echo Resource service...
mvn -f resource_service/ -DskipTests clean package

echo Song service...
mvn -f song_service/ -DskipTests clean package

echo OK.


