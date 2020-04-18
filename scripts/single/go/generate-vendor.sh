#/bin/bash
set -e
set -x

rm -rf vendor

go mod vendor
