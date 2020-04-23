#!/bin/sh

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../../repository/CONFIGURATION
source $(dirname $(realpath ${BASH_SOURCE[0]}))/../../utils/bash.sh
source $(dirname $(realpath ${BASH_SOURCE[0]}))/../../utils/git.sh

update_gradle_scripts()
{
  result=0
  execute_command pushd scripts/build-gradle-scripts
  git_checkout_branch master
  git_checkout_branch_result=$?
  if [ $git_checkout_branch_result -eq 0 ]
  then
    git_pull
    git_pull_result=$?
    result=$git_pull_result
  else
    result=$git_checkout_branch_result
  fi
  execute_command popd
  return $result
}

update_gradle_wrapper()
{
  GRADLE_VERSION=$1
  result=0
  echo Updating gradle wrapper to version $GRADLE_VERSION
  execute_command ./gradlew :wrapper --gradle-version $GRADLE_VERSION --distribution-type all
  gradle_wrapper_result=$?
  if [ $gradle_wrapper_result -eq 0 ]
  then
    echo Gradle wrapper updated.
  else
    echo Failed updating gradle wrapper
    result=$gradle_wrapper_result
  fi
  return $result
}

GRADLE_VERSION=6.3
result=0
previous_repository_name="codingame"
for REPOSITORY_NAME in $JAVA_REPOSITORY_NAMES
do
  execute_command pushd $REPOSITORY_NAME
  update_gradle_scripts
  update_gradle_scripts_result=$?
  if [ $update_gradle_scripts_result -eq 0 ]
  then
    if [ ! -z "$previous_repository_name" ]
    then
      execute_command cp ../$previous_repository_name/gradle/wrapper/gradle-wrapper.jar gradle/wrapper/gradle-wrapper.jar
      execute_command cp ../$previous_repository_name/gradle/wrapper/gradle-wrapper.properties gradle/wrapper/gradle-wrapper.properties
      execute_command cp ../$previous_repository_name/gradlew gradlew
      execute_command cp ../$previous_repository_name/gradlew.bat gradlew.bat
    fi
    update_gradle_wrapper $GRADLE_VERSION
    update_gradle_wrapper_result=$?
    result=$update_gradle_wrapper_result
  else
    result=$update_gradle_scripts_result
  fi
  execute_command popd
  exit_on_error $result

  previous_repository_name=$REPOSITORY_NAME
done
