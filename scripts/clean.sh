#!/bin/bash
set -e
set -x

source scripts/CONFIGURATION

for project in $PROJECTS; do
    pushd $project
    bash ./scripts/clean.sh
    popd
done

rm -rf k8s