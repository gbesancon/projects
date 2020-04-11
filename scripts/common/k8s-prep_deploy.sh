#!/bin/bash
set -e
set -x

export VERSION=$1
export ENVIRONMENT_NAME=$2

source scripts/env/env.$ENVIRONMENT_NAME.sh

cat deployment.yaml \
    | sed "s#\$DOCKER_IMAGE_PREFIX#$DOCKER_IMAGE_PREFIX#g" \
    | sed "s#\$PROJECT_NAME#$PROJECT_NAME#g" \
    | sed "s#\$VERSION#$VERSION#g" \
    | sed "s#\$ENVIRONMENT_NAME#$ENVIRONMENT_NAME#g" \
    | sed "s#\$CNAME#$CNAME#g" \
    | sed "s#\$IMAGE_PULL_POLICY#$IMAGE_PULL_POLICY#g" \
    > deployment-$VERSION-$ENVIRONMENT_NAME.yaml