#!/bin/bash

check_usage ()
{
  if [ $# -eq 1 ]
    then
      if [[ ! -f $1 ]]; then
        echo "Filepath $1 do not exist."
        exit 1
      fi
      echo "Start $0 :"
      echo " - Filepath : $1"
    else
      echo "Usage: $0 FILEPATH"
      exit 1
  fi
}

check_tools ()
{
  command -v cat > /dev/null 2>&1 || { echo >&2 "cat is not installed.  Aborting."; exit 1; }
  command -v aplay> /dev/null 2>&1 || { echo >&2 "aplay is not installed.  Aborting."; exit 1; }
}

listen_file ()
{
  cat $1 | aplay
}

check_usage "$@"
check_tools
listen_file $1 

