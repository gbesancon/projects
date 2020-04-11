#!/bin/bash
set -e
set -x

export VERSION=$1

docker stack deploy gbesancon --compose-file docker-compose.yml
