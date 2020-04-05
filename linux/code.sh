#!/bin/sh
#set -e
#set -x

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../utils/apt.sh

# https://code.visualstudio.com/docs/setup/linux
setup_microsoft_package_repository()
{
  echo Setup microsoft package repository
  curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > packages.microsoft.gpg
  sudo install -o root -g root -m 644 packages.microsoft.gpg /usr/share/keyrings/
  sudo sh -c 'echo "deb [arch=amd64 signed-by=/usr/share/keyrings/packages.microsoft.gpg] https://packages.microsoft.com/repos/vscode stable main" > /etc/apt/sources.list.d/vscode.list'
}

install_or_update_vscode() {
  result=0
  check_command code
  check_command_result=$?
  if [ $check_command_result -eq 0 ]
  then
    update_packages code
    result=$?
  else
    setup_microsoft_package_repository
    setup_microsoft_package_repository_result=$?
    if [ $setup_microsoft_package_repository_result -eq 0 ]
    then
      update_packages code
      result=$?
    else
      result=$setup_microsoft_package_repository_result
    fi
  fi
  return $result
}

install_or_update_vscode