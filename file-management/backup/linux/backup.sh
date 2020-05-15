#!/bin/bash

check_tools ()
{
  command -v rsync > /dev/null 2>&1 || { echo >&2 "rsync is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -eq 2 ]
    then
      if ! [[ -d "$1" ]]; then
        echo "Source folder $1 does not exist."
        exit 1
      fi
      if ! [[ -d "$2" ]]; then
        echo "Target folder $2 does not exist."
        exit 1
      fi
      echo "Start $0 :"
      echo " - Source folder : $1"
      echo " - Target folder : $2"
    else
      echo "Usage: $0 SOURCE_FOLDER TARGET_FOLDER"
      exit 1
  fi
}

backup ()
{
  run_rsync "$1/" "$2"
  echo `date` > "$2/timestamp"
}

run_rsync ()
{
  echo "Synchronizing $1 -> $2" 
  # a : Archive mode (equals -rlptgoD)
  # u : Update
  # v : Verbose
  # z : Transfert 
  # --delete : Delete target files with no matching source file
  # --progress  : Display progress
  rsync -auvzr --dry-run --delete --progress "$1" "$2"
}

check_tools
check_usage "$@"
backup "$@"
