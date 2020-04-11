#!/bin/sh
#set -e
#set -x

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../utils/apt.sh

for command in cc g++ gcc make cmake; do
  install_command_from_package $command $command
  exit_on_error $?
done
