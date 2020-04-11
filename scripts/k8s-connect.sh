#!/bin/bash
set -e
set -x

export ENVIRONMENT_NAME=$1

gcloud container clusters get-credentials $ENVIRONMENT_NAME --zone us-central1-a --project gbesancon