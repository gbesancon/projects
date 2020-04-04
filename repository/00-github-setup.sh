#!/bin/sh

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../linux/utils-github.sh
source $(dirname $(realpath ${BASH_SOURCE[0]}))/../linux/utils-git.sh

REPOSITORY_NAME=setup
git_clone_repository $(get_github_repository_url gbesancon $REPOSITORY_NAME) $REPOSITORY_NAME
exit_on_error $?
