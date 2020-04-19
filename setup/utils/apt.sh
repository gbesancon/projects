#!/bin/sh

source $(dirname $(realpath ${BASH_SOURCE[0]}))/bash.sh

add_ppa_package_repository()
{
  PPA=$1
  result=0
  echo Adding apt-repository $PPA
  execute_command sudo add-apt-repository -y ppa:$PPA
  add_apt_repository_result=$?
  if [ $add_apt_repository_result -eq 0 ]
  then
    echo apt-repository $PPA added
    refresh_packages
    result=$?
  else
    echo Failed to add apt-repository $PPA
    result=$add_apt_repository_result
  fi
  return $result
}

refresh_packages()
{
  result=0
  echo Refreshing packages
  execute_command sudo apt-get update
  update_result=$?
  if [ $update_result -eq 0 ]
  then
    echo Packages refreshed 
  else
    echo Failed to refresh packages
  fi
  result=$update_result
  return $result
}

install_packages()
{
  PACKAGES=$@
  result=0
  for PACKAGE in $PACKAGES
  do
    install_package $PACKAGE
    install_package_result=$?
    if [ $install_package_result -ne 0 ]
    then
      result=$install_package_result
    fi
  done
  return $result
}

install_package()
{
  PACKAGE=$1
  result=0
  echo Installing package $PACKAGE
  execute_command sudo DEBIAN_FRONTEND=noninteractive apt-get install -y $PACKAGE
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

update_packages()
{
  PACKAGES=$@
  result=0
  refresh_packages
  refresh_packages_result=$?
  if [ $refresh_packages_result -eq 0 ]
  then
    echo Updating packages $PACKAGES
    install_packages $PACKAGES
    update_packages_result=$?
    result=$update_packages_result
  else
    result=$refresh_packages_result
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
