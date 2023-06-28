#!/bin/bash
set -eu

sudo docker-compose down

./rebuild_all.sh

sudo docker-compose up --build -d

