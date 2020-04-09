#!/bin/sh

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../repository/CONFIGURATION
source $(dirname $(realpath ${BASH_SOURCE[0]}))/../utils/git.sh

REPOSITORY_NAMES=setup

for REPOSITORY_NAME in $REPOSITORY_NAMES
do
  result=0
  echo Processing repository $REPOSITORY_NAME
  git_add $REPOSITORY_NAME
  git_add_result=$?
  if [ $git_add_result -eq 0 ]
  then
    git_status
    git_status_result=$?
    if [ $git_status_result -eq 0 ]
    then
      git_commit "$MESSAGE"
      git_commit_result=$?
      if [ $git_commit_result -ne 0 ]
      then
        result=$git_commit_result
      fi
    else
      result=$git_status_result
    fi
  else
    result=$git_add_result
  fi
  exit_on_error $result
done
