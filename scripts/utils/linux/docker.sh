#!/bin/sh
set -e
set -x

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../bash.sh

# https://www.linode.com/docs/applications/containers/install-docker-ce-ubuntu-1804/
install_docker_ubuntu()
{
  result=0
  check_command code
  check_command_result=$?
  if [ $check_command_result -eq 0 ]
  then
    sudo apt-get remove docker docker-engine docker.io
    sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common gnupg
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo apt-key fingerprint 0EBFCD88
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
    sudo apt-get update
    sudo apt-get install -y docker-ce
    sudo usermod -aG docker $USER
  fi
  return $result
}

# https://linuxhint.com/install_docker_linux_mint/
install_docker_linuxmint()
{
  result=0
  check_command code
  check_command_result=$?
  if [ $check_command_result -eq 0 ]
  then
    sudo apt-get remove docker docker-engine docker.io containerd runc  
    sudo apt-get update
    sudo apt-get install -y apt-transport-https ca-certificates curl gnupg-agent software-properties-common
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo apt-key fingerprint 0EBFCD88
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
    sudo apt-get update
    sudo apt-get install -y docker-ce docker-ce-cli containerd.io
    sudo usermod -aG docker $USER
  fi
  return $result
}

install_docker_linuxmint

