#!/bin/sh

source $(dirname $(realpath ${BASH_SOURCE[0]}))/bash.sh

add_ppa_package_repository()
{
  PPA=$1
  result=0
  echo Adding apt-repository $PPA
  execute_command sudo add-apt-repository ppa:$PPA
  add_apt_repository_result=$?
  if [ $add_apt_repository_result -eq 0 ]
  then
    echo apt-repository $PPA added
    update_package
    result=$?
  else
    echo Failed to add apt-repository $PPA
    result=$add_apt_repository_result
  fi
  return $result
}

install_package()
{
  PACKAGE=$1
  result=0
  echo Installing package $PACKAGE
  execute_command sudo apt-get install -y $PACKAGE
  install_package_result=$?
  if [ $install_package_result -eq 0 ]
  then
    echo Package $PACKAGE installed
  else
    echo Failed to install package $PACKAGE
    result=$install_package_result
  fi
  return $result
}

update_package()
{
  PACKAGE=$1
  result=0
  echo Updating package $PACKAGE
  execute_command sudo apt-get update
  update_result=$?
  if [ $update_result -eq 0 ]
  then
    install_package $PACKAGE
    update_package_result=$?
    result=$update_package_result
  else
    echo Failed to update packages
    result=$update_result
  fi
  return $result
}

install_command_from_package()
{
  COMMAND=$1
  PACKAGE=$2
  result=0
  check_command $COMMAND
  check_command_result=$?
  if [ $check_command_result -ne 0 ]
  then
    install_package $PACKAGE
    install_package_result=$?
    result=$install_command_package
  fi
  return $result
}
