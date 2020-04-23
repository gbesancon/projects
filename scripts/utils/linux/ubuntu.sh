#!/bin/sh
#set -e
#set -x

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../utils/apt.sh

install_command_from_package apt-get apt-utils
exit_on_error $?

for command in apt bash curl dialog git wget; do
  install_command_from_package $command $command
  exit_on_error $?
done
