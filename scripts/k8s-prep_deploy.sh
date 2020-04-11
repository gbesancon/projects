#!/bin/bash
set -e
set -x

export VERSION=$1
export ENVIRONMENT_NAME=$2

source scripts/CONFIGURATION

for project in $PROJECTS; do
    pushd $project
    bash ./scripts/k8s-prep_deploy.sh $VERSION $ENVIRONMENT_NAME
    popd
    if [ -f ./$project/deployment-$VERSION-$ENVIRONMENT_NAME.yaml ]; then
      mkdir -p k8s/$VERSION/$ENVIRONMENT_NAME
      cp ./$project/deployment-$VERSION-$ENVIRONMENT_NAME.yaml k8s/$VERSION/$ENVIRONMENT_NAME/deployment-$project.yaml
    fi
done