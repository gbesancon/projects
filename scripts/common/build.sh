#!/bin/bash
set -e
set -x

export VERSION=$1

make build VERSION=${VERSION}
make test
