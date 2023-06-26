#!/bin/bash
kill `cat song_service/run.pid` && rm song_service/run.pid
kill `cat resource_service/run.pid` && rm resource_service/run.pid
