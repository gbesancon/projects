#!/bin/sh

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../repository/CONFIGURATION
source $(dirname $(realpath ${BASH_SOURCE[0]}))/../utils/github.sh
source $(dirname $(realpath ${BASH_SOURCE[0]}))/../utils/git.sh

for REPOSITORY_NAME in $REPOSITORY_NAMES
do
  git_clone_repository $(get_github_repository_url gbesancon $REPOSITORY_NAME) $REPOSITORY_NAME
  exit_on_error $?
done