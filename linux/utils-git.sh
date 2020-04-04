#!/bin/sh

source $(dirname $(realpath ${BASH_SOURCE[0]}))/utils.sh

git_clone_repository()
{
  URL=$1
  FOLDER_PATH=$2
  result=0
  delete_folder $FOLDER_PATH
  delete_folder_result=$?
  if [ $delete_folder_result -eq 0 ]
  then
    echo Cloning repository $URL
    execute_command git clone $URL $FOLDER_PATH
    git_clone_result=$?
    if [ $git_clone_result -eq 0 ]
    then
      echo Repository $URL cloned.
    else
      echo Failed cloning repository $URL.
      result=$git_clone_result
    fi
  fi
  return $result
}