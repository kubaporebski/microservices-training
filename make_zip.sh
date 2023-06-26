#!/bin/bash
set -eu
if [[ -f plik.zip ]]; then
  rm plik.zip
fi
zip -9 -r plik.zip resource_service -x resource_service/target/\* -x resource_service/output.log -x resource_service/run.pid
zip -9 -r plik.zip song_service -x song_service/target/\* -x song_service/output.log -x song_service/run.pid
zip -9 -r plik.zip scripts
zip -9 -r plik.zip remove.sh run.sh stop.sh
zip -9 -r plik.zip docker-compose.yaml