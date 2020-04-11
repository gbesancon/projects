#!/bin/bash
set -e
set -x

export VERSION=$1

bash scripts/docker-build.sh $VERSION