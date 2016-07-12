#!/bin/bash

check_tools ()
{
  command -v git > /dev/null 2>&1 || { echo >&2 "git is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -eq 1 ]
    then
      if ! [[ -d "$1" ]]; then
        echo "Source folder $1 does not exist."
        exit 1
      fi
      echo "Start $0 :"
      echo " - Source folder : $1"
    else
      echo "Usage: $0 SOURCE_FOLDER"
      exit 1
  fi
}

cloc()
{
  git clone https://github.com/AlDanial/cloc.git 
  cloc/cloc $1
  rm -rf cloc  
}

check_tools
check_usage "$@"
cloc "$1"
