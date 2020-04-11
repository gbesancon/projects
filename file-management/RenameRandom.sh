#!/bin/bash

check_tools ()
{
  command -v echo > /dev/null 2>&1 || { echo >&2 "echo is not installed.  Aborting."; exit 1; }
  command -v pwd > /dev/null 2>&1 || { echo >&2 "pwd is not installed.  Aborting."; exit 1; }
  command -v cat > /dev/null 2>&1 || { echo >&2 "cat is not installed.  Aborting."; exit 1; }
  command -v tr > /dev/null 2>&1 || { echo >&2 "tr is not installed.  Aborting."; exit 1; }
  command -v head > /dev/null 2>&1 || { echo >&2 "head is not installed.  Aborting."; exit 1; }
  command -v mv > /dev/null 2>&1 || { echo >&2 "mv is not installed.  Aborting."; exit 1; }
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

rename_files ()
{
  for FILENAME in $1; do
    [ -f "$FILENAME" ] || continue # if not a file, skip
    EXTENSION=`echo "${FILENAME##*.}" | tr '[:upper:]' '[:lower:]'` # lowercase extension
    NEW_FILENAME="$2"`cat /dev/urandom | tr -cd 'a-f0-9' | head -c 32`."$EXTENSION" # new filename
    #echo Rename $FILENAME $NEW_FILENAME
    mv "$FILENAME" "$NEW_FILENAME" > /dev/null  2> /dev/null
  done
}

rename_files_folders () 
{
  echo Processing `pwd`/$1
  rename_files "*.*"
}

rename_photos ()
{
   (cd "$1" ; rename_files_folders "$1" ; cd "$REFERENCE_FOLDER")
}

REFERENCE_FOLDER=`pwd`
check_tools
check_usage "$@"
rename_photos "$@"

