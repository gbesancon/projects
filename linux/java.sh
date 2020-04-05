#!/bin/sh
#set -e
#set -x

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../utils/apt.sh

install_jdk()
{
  JAVA_VERSION=$1
  result=0
  echo Installing java version $JAVA_VERSION
  add_ppa_package_repository linuxuprising/java
  add_ppa_package_repository_result=$?
  if [ $add_ppa_package_repository_result -eq 0 ]
  then
    update_packages oracle-java${JAVA_VERSION}-installer oracle-java${JAVA_VERSION}-set-default
    update_packages_result=$?
    result=$update_packages_result
  else
    result=$add_ppa_package_repository_result
  fi
  return $result
}

install_or_update_jdk()
{
  JAVA_VERSION=$1
  result=0
  check_command java
  check_command_result=$?
  if [ $check_command_result -eq 0 ]
  then
    CURRENT_JAVA_VERSION=`java -version 2>&1 | grep -o -e "version \"[[:digit:]]\+" | grep -o -e "[[:digit:]]\+"`
    if [ $CURRENT_JAVA_VERSION == $JAVA_VERSION ]
    then
      echo java version $JAVA_VERSION available
    else
      echo java version $CURRENT_JAVA_VERSION available
      install_jdk $JAVA_VERSION
      install_jdk_result=$?
      result=$install_jdk_result
    fi
  else
    install_jdk $JAVA_VERSION
    install_jdk_result=$?
    result=$install_jdk_result
  fi
  return $result
}

install_or_update_jdk 14