#!/bin/bash

BASH=`which bash`

remove_image()
{
  DOCKER_REMOVE_COMMAND="$BASH $BUILD_SOURCESDIRECTORY/scripts/build-gradle/build/remove-docker-image.sh $buildDockerImageTag"
  echo $DOCKER_REMOVE_COMMAND
  $DOCKER_REMOVE_COMMAND
}

if [ "$#" -eq 0 ]; then
    buildDockerImageFolder="$BUILD_SOURCESDIRECTORY/scripts/build-gradle/build/docker"
else
    if [ "$#" -eq 1 ]; then
        buildDockerImageFolder="$1"
    else
        echo "Usage:"
        echo "- $0"
        echo "- $0 <BUILD_DOCKER_IMAGE_FOLDER>"
        exit 1
    fi
fi

echo "Start build:"
echo "Branch: $BUILD_SOURCEBRANCH"

buildDockerImageTag="$BUILD_REPOSITORY_NAME:$BUILD_BUILDNUMBER"

echo "Create the docker container image for building."
DOCKER_BUILD_COMMAND="$BASH $BUILD_SOURCESDIRECTORY/scripts/build-gradle/build/create-docker-image.sh $buildDockerImageFolder $buildDockerImageTag"
echo $DOCKER_BUILD_COMMAND
$DOCKER_BUILD_COMMAND
buildDockerStatus=$?
if [ $buildDockerStatus -ne 0 ]; then 
  echo "Creation of the docker container image for building failed."
  exit $buildDockerStatus
else
  echo "Creation of the docker container image for building successful."
fi

GRADLE_OPTIONS="--no-daemon --profile"
GRADLE_FLAGS="-Dorg.gradle.project.checkGoogleJavaFormatEnabled=$CHECKGOOGLEJAVAFORMATENABLED -Dorg.gradle.project.checkCheckstyleEnabled=$CHECKCHECKSTYLEENABLED -Dorg.gradle.project.checkSpotBugsEnabled=$CHECKSPOTBUGSENABLED -Dorg.gradle.project.checkPmdEnabled=$CHECKPMDENABLED -Dorg.gradle.project.packageSourcesEnabled=$PACKAGESOURCESENABLED -Dorg.gradle.project.packageTestsEnabled=$PACKAGETESTSENABLED -Dorg.gradle.project.generateDocumentationEnabled=$GENERATEDOCUMENTATIONENABLED -Dorg.gradle.project.runIntegrationTestEnabled=$RUNINTEGRATIONTESTENABLED -Dorg.gradle.project.generateLicenseReportEnabled=$GENERATELICENSEREPORTENABLED -Dorg.gradle.project.generateOwaspReportEnabled=$GENERATEOWASPREPORTENABLED -Dorg.gradle.project.generateJacocoReportEnabled=$GENERATEJACOCOREPORTENABLED -Dorg.gradle.project.publishSonarqubeReportEnabled=$PUBLISHSONARQUBEREPORTENABLED"

echo "Generate type file using the docker image."
GRADLE_TARGETS="generateTypeFile"
GRADLE_COMMAND="$BASH $BUILD_SOURCESDIRECTORY/scripts/build-gradle/build/run-docker-image.sh $buildDockerImageTag sh ./gradlew $GRADLE_OPTIONS $GRADLE_TARGETS $GRADLE_FLAGS"
echo $GRADLE_COMMAND
$GRADLE_COMMAND
generateTypeFileStatus=$?
if [ $generateTypeFileStatus -ne 0 ]; then 
  echo "Generating type file using the docker image failed."
  remove_image
  exit $generateTypeFileStatus
else
  echo "Generating type file using the docker image successful."
fi

type=$(head -n 1 $BUILD_SOURCESDIRECTORY/TYPE)
echo "Type: $type"

if [[ $type == "RELEASE" ]] && [[ $BUILD_SOURCEBRANCH == refs/heads/master ]]; then
  echo "Only snapshots can be built in the master branch."
  remove_image
  exit 1
fi
if [[ $type == "RELEASE" ]] && [[ $BUILD_SOURCEBRANCH != refs/heads/release/* ]]; then
  echo "Release can only be built in a release branch."
  remove_image
  exit 1
fi

echo "Build branch $BUILD_SOURCEBRANCH using the docker image."
case "$BUILD_SOURCEBRANCH" in 
  refs/heads/master) GRADLE_TARGETS="clean build distribute report publish" ;;
  refs/heads/release/*) GRADLE_TARGETS="clean build distribute report publish" ;;
  refs/pull/*) GRADLE_TARGETS="clean build distribute report" ;;
  refs/heads/feature/*) GRADLE_TARGETS="clean build distribute report" ;;
  refs/heads/bug/*) GRADLE_TARGETS="clean build distribute report" ;;
  refs/tags/*) GRADLE_TARGETS="clean build distribute report" ;;
  *) echo "Build not allowed." && exit 1 ;;
esac
GRADLE_COMMAND="$BASH $BUILD_SOURCESDIRECTORY/scripts/build-gradle/build/run-docker-image.sh $buildDockerImageTag sh ./gradlew $GRADLE_OPTIONS $GRADLE_TARGETS $GRADLE_FLAGS"
echo $GRADLE_COMMAND
$GRADLE_COMMAND
buildReportStatus=$?
if [ $buildReportStatus -eq 0 ]; then 
  echo "Building using the docker image successful."
    
  if [ ! -f $BUILD_SOURCESDIRECTORY/VERSION ] || [ ! -f $BUILD_SOURCESDIRECTORY/TYPE ] || [ ! -f $BUILD_SOURCESDIRECTORY/FULL_VERSION ] || [ ! -f $BUILD_SOURCESDIRECTORY/TAG ] || [ ! -f $BUILD_SOURCESDIRECTORY/MAVEN_VERSION ]; then
    echo "Version files missing."
    remove_image
    exit 1
  fi  
else
  echo "Building using the docker image failed."

  echo "Zipping all the report using the docker image."
  GRADLE_TARGETS="zipAllReport"
  GRADLE_COMMAND="$BASH $BUILD_SOURCESDIRECTORY/scripts/build-gradle/build/run-docker-image.sh $buildDockerImageTag sh ./gradlew $GRADLE_OPTIONS $GRADLE_TARGETS $GRADLE_FLAGS"
  echo $GRADLE_COMMAND
  $GRADLE_COMMAND
  zipAllReportStatus=$?
  if [ $zipAllReportStatus -ne 0 ]; then
    echo "Zipping all reports using the docker image failed."
    exit $zipAllReportStatus
  else
    echo "Zipping all reports using the docker image successful."
    exit $buildReportStatus
  fi
fi

remove_image
