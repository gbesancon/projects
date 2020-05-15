#!/bin/bash

check_tools ()
{
  command -v find > /dev/null 2>&1 || { echo >&2 "find is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -eq 2 ]
    then
      if ! [[ -d "$1" ]]; then
        echo "The input folder $1 does not exist."
        exit 1
      fi
      if ! [[ -d "$2" ]]; then
        echo "The output folder $2 does not exist."
        exit 1
      fi
      echo "Start $0 :"
      echo " - Input folder : $1"
      echo " - Output folder : $2"
    else
      echo "Usage: $0 INPUT_FOLDER OUTPUT_FOLDER"
      exit 1
  fi
}

create_hierarchy ()
{
  (cd "$1" ; find -type d -printf '%h/%f\n') |
  while read FOLDER; do # split other \n
    echo Creating $2$FOLDER
    mkdir "$2$FOLDER" > /dev/null  2> /dev/null
  done
}

REFERENCE_FOLDER=`pwd`
check_tools
check_usage "$@"
create_hierarchy "$@"

