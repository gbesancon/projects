#!/bin/bash

check_tools ()
{
  command -v echo > /dev/null 2>&1 || { echo >&2 "echo is not installed.  Aborting."; exit 1; }
  command -v /usr/share/fslint/fslint/findup > /dev/null 2>&1 || { echo >&2 "fslint/findup is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -eq 1 ]
    then
      if ! [[ -d "$1" ]]; then
        echo "The folder $1 does not exist."
        exit 1
      fi
      echo "Start $0 :"
      echo " - Folder : $1"
    else
      echo "Usage: $0 FOLDER"
      exit 1
  fi
}

check_duplicates ()
{
  /usr/share/fslint/fslint/findup $1 --summary 
}

check_tools
check_usage "$@"
check_duplicates "$@"

