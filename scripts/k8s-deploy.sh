#!/bin/bash
set -e
set -x

export VERSION=$1
export ENVIRONMENT_NAME=$2

source scripts/CONFIGURATION

for project in $PROJECTS; do
    pushd $project
    bash ./scripts/k8s-deploy.sh $VERSION $ENVIRONMENT_NAME
    popd
done
