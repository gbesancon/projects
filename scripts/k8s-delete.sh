#!/bin/bash
#set -e
set -x

source scripts/CONFIGURATION

for project in $PROJECTS; do
    pushd $project
    bash ./scripts/k8s-delete.sh
    popd
done
