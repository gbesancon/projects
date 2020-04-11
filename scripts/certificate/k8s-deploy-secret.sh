#!/bin/bash
set -e
set -x

ENVIRONMENT_NAME=$1

SECRET_NAME=ssl

bash scripts/k8s-connect.sh $ENVIRONMENT_NAME
kubectl delete secret $SECRET_NAME
kubectl create secret tls $SECRET_NAME --key scripts/certificate/$ENVIRONMENT_NAME/server.key --cert scripts/certificate/$ENVIRONMENT_NAME/entrust/ServerCertificateWithChain.crt
