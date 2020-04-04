#!/bin/sh
#set -e
#set -x

check_command()
{
  COMMAND=$1
  result=0
  echo Checking $COMMAND is available
  command -v $COMMAND > /dev/null
  command_result=$?
  if [ $command_result -eq 0 ]
  then
    echo $COMMAND available
  else
    echo $COMMAND not available
    result=$command_result
  fi
  return $result
}

install_package()
{
  PACKAGE=$1
  result=0
  echo Installing package $PACKAGE
  sudo apt-get install $PACKAGE > /dev/null
  install_package_result=$?
  if [ $install_package_result -neq 0]
  then
    echo Failed to install package $PACKAGE
    result=$install_package_result
  fi
  exit $result
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

exit_on_error()
{
  if [ $1 -ne 0 ]; then exit $1; fi
}

install_command_from_package curl
exit_on_error $?

install_command_from_package git
exit_on_error $?

# VSCode

