#!/bin/bash
set -e
set -x

export VERSION=$1

source ../scripts/CONFIGURATION

docker push $DOCKER_IMAGE_PREFIX/$PROJECT_NAME:$VERSION