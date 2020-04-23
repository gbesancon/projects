#!/bin/bash
# https://www.sslshopper.com/article-most-common-openssl-commands.html

set -e

CNAME=$1

echo Generate certificate request for $CNAME

PASSWORD=abcdefg

rm -rf scripts/certificate/$CNAME
rm -rf scripts/certificate/$CNAME.zip

mkdir scripts/certificate/$CNAME

pushd scripts/certificate/$CNAME

echo Generate server key
openssl genrsa -des3 -out server.key -passout pass:$PASSWORD 2048

echo Remove password
openssl rsa -in server.key -out server.key -passin pass:$PASSWORD

C=FR
S=FR
L=Mont-de-Marsan
O=
OU=
CN=$CNAME
E=gilles.besancon@gmail.com

echo Generate certificate request
printf "$C\n$S\n$L\n$O\n$OU\n$CN\n$E\n\n\n" | openssl req -new -key server.key -out server.csr

#echo Generate certificate for 2 years
#openssl x509 -req -days 730 -in server.csr -signkey server.key -out server.crt

echo "Verify private key"
openssl rsa -in server.key -check

echo "Verify Certificate Signing Request (CSR)"
openssl req -text -noout -verify -in server.csr
cat server.csr

#echo "Verify Certificate (CRT)"
#openssl x509 -text -noout -in server.crt

pushd ..

echo "Zip key, certificate request and certificate"
zip -r $CNAME.zip $CNAME/
rm -rf $CNAME