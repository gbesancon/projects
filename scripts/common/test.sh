#!/bin/bash
set -e
set -x

source ../scripts/CONFIGURATION

make test PROJECT_NAME=${PROJECT_NAME}
