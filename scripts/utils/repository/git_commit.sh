#!/bin/sh

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../repository/CONFIGURATION
source $(dirname $(realpath ${BASH_SOURCE[0]}))/../utils/git.sh

MESSAGE="$1"

if [ $# -gt 1 ]
then
  shift
  REPOSITORY_NAMES=$@
fi

result=0
for REPOSITORY_NAME in $REPOSITORY_NAMES
do
  echo Processing repository $REPOSITORY_NAME
  execute_command pushd $REPOSITORY_NAME
  git_add_all $REPOSITORY_NAME
  git_add_all_result=$?
  if [ $git_add_all_result -eq 0 ]
  then
    git_status
    git_status_result=$?
    if [ $git_status_result -eq 0 ]
    then
      git_commit "$MESSAGE"
      git_commit_result=$?
      if [ $git_commit_result -eq 0 ]
      then
        git_push
        git_push_result=$?
        if [ $git_push_result -ne 0 ]
        then
          result=$git_push_result
        fi
      else
        result=$git_commit_result
      fi
    else
      result=$git_status_result
    fi
  else
    result=$git_add_all_result
  fi
  execute_command popd
  exit_on_error $result
done
