#!/bin/sh
#set -e
#set -x

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../../../utils/apt.sh

get_java_version()
{
  JAVA_VERSION=`java -version 2>&1 | sed 's/\"1\./\"/g' | sed 's/_/\./g' | grep -o -e "version \"[[:digit:]]\+" | grep -o -e "[[:digit:]]\+"`
  echo $JAVA_VERSION  
}

install_openjdk_jdk()
{
  JAVA_VERSION=$1
  result=0
  echo Installing java version $JAVA_VERSION
  install_package openjdk-$JAVA_VERSION-jdk
  install_package_result=$?
  if [ $install_package_result -eq 0 ]
  then
    execute_command sudo update-java-alternatives -s java-1.$JAVA_VERSION.0-openjdk-amd64
    update_java_alternatives_result=$?
    if [ $update_java_alternatives_result -eq 0 ]
    then
      echo java version $JAVA_VERSION set as default
    else
      echo Failed to set java version $JAVA_VERSION as default
      result=$update_java_alternatives_result
    fi
  else
    result=$install_package_result
  fi
  return $result
}

install_oracle_jdk()
{
  JAVA_VERSION=$1
  result=0
  echo Installing java version $JAVA_VERSION
  add_ppa_package_repository linuxuprising/java
  add_ppa_package_repository_result=$?
  if [ $add_ppa_package_repository_result -eq 0 ]
  then
    execute_command update_packages oracle-java${JAVA_VERSION}-installer oracle-java${JAVA_VERSION}-set-default
    update_packages_result=$?
    if [ $update_packages_result -eq 0 ]
    then
      sudo update-java-alternatives -s java-$JAVA_VERSION-oracle
      update_java_alternatives_result=$?
      if [ $update_java_alternatives_result -eq 0 ]
      then
        echo java version $JAVA_VERSION set as default
      else
        echo Failed to set java version $JAVA_VERSION as default
        result=$update_java_alternatives_result
      fi
    else
      result=$update_packages_result
    fi
  else
    result=$add_ppa_package_repository_result
  fi
  return $result
}

install_or_update_jdk()
{
  JDK_TYPE=$1
  JAVA_VERSION=$2
  result=0
  check_command java
  check_command_result=$?
  if [ $check_command_result -eq 0 ]
  then
    CURRENT_JAVA_VERSION=`get_java_version`
    if [ $CURRENT_JAVA_VERSION == $JAVA_VERSION ]
    then
      echo java version $JAVA_VERSION available
    else
      echo java version $CURRENT_JAVA_VERSION available
      install_${JDK_TYPE}_jdk $JAVA_VERSION
      install_jdk_result=$?
      result=$install_jdk_result
    fi
  else
    install_${JDK_TYPE}_jdk $JAVA_VERSION
    install_jdk_result=$?
    result=$install_jdk_result
  fi
  return $result
}

# oracle / openjdk
JDK_TYPE=$1
JAVA_VERSION=$2

install_or_update_jdk $JDK_TYPE $JAVA_VERSION
exit_on_error $?
