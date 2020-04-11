#!/bin/bash
set -e
set -x

export VERSION=$1
export ENVIRONMENT_NAME=$2

kubectl apply -f deployment-$VERSION-$ENVIRONMENT_NAME.yaml