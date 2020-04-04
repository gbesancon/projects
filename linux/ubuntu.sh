#!/bin/sh
#set -e
#set -x

source utils.sh

for command in bash curl g++ gcc git go wget; do
  install_command_from_package $command $command
  exit_on_error $?
done
