#!/bin/bash
#set -e
set -x

source ../scripts/CONFIGURATION

kubectl delete configmap,service,deployment $PROJECT_NAME
kubectl delete ingress $PROJECT_NAME-public $PROJECT_NAME-protected
