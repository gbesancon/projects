#!/bin/sh
#set -e
#set -x

source utils.sh

install_or_update_jdk()
{
  JAVA_VERSION=$1
  result=0
  check_command java
  check_command_result=$?
  if [ $check_command_result -eq 0 ]
  then
    update_package oracle-java${JAVA_VERSION}-installer
  else
    add_ppa_package_repository linuxuprising/java
    update_package oracle-java${JAVA_VERSION}-installer
  fi
  return $result
}

install_or_update_jdk 14