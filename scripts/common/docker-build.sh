#!/bin/bash
set -e
set -x

export VERSION=$1

source ../scripts/CONFIGURATION

docker build --build-arg VERSION=${VERSION} -t $DOCKER_IMAGE_PREFIX/$PROJECT_NAME:$VERSION -f Dockerfile ..

image_id=$(docker create $DOCKER_IMAGE_PREFIX/$PROJECT_NAME:$VERSION)
for file in "coberturaTestReport.xml" "TEST-${PROJECT_NAME}.xml"
do
    docker cp ${image_id}:/app/build/test-results/$file ./$file
done
docker rm -v $image_id