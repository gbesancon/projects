#!/bin/bash
set -e 
set -x

ENVIRONMENT_NAME=$1

openssl rsa -in scripts/certificate/$ENVIRONMENT_NAME/server.key -check
openssl x509 -in scripts/certificate/$ENVIRONMENT_NAME/entrust/ServerCertificate.crt -text -noout

KEY_MODULUS=`openssl rsa -modulus -noout -in scripts/certificate/$ENVIRONMENT_NAME/server.key`
CERTIFICATE_MODULUS=`openssl x509 -modulus -noout -in scripts/certificate/$ENVIRONMENT_NAME/entrust/ServerCertificate.crt`
if [ "$KEY_MODULUS" == "$CERTIFICATE_MODULUS" ]; then
    echo "Key and Certificate valid !"
    exit 0
else
    echo "Key do not match the certificate !"
    exit 1
fi
