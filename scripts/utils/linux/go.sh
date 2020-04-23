#!/bin/sh
#set -e
#set -x

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../utils/apt.sh

install_command_from_package go golang-go
exit_on_error $?
