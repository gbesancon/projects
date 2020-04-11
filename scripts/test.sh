#!/bin/bash
set -e
set -x

export VERSION=$1

source scripts/CONFIGURATION

for project in $PROJECTS; do
    pushd $project
    bash ./scripts/test.sh $VERSION
    popd
done
