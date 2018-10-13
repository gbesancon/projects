#!/bin/bash

check_tools ()
{
  command -v echo > /dev/null 2>&1 || { echo >&2 "echo is not installed.  Aborting."; exit 1; }
  command -v cd > /dev/null 2>&1 || { echo >&2 "cd is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -eq 1 ]
    then
      if ! [[ -d "$1" ]]; then
        echo "The photos folder $1 does not exist."
        exit 1
      fi
      echo "Start $0 :"
      echo " - Photos top folder : $1"
    else
      echo "Usage: $0 PHOTOS_TOP_FOLDER"
      exit 1
  fi
}

check_files ()
{
  COUNTER=1
  for FILENAME in *; do
    [ -f "$FILENAME" ] || continue # if not a file, skip
    if ! [[ $FILENAME =~ $1 ]]; then 
      if ! [[ $FILENAME =~ $2 ]]; then
        if ! [[ $FILENAME =~ $3 ]]; then
          echo `pwd`/$FILENAME
        fi
      fi
    fi
  done
}

check_folders ()
{
  for FOLDER in *; do
    [ -d "$FOLDER" ] || continue # if not a directory, skip
    cd "$FOLDER"
    check_files_folders "$FOLDER"
    cd ..
  done
}

check_files_folders () 
{
  #echo Processing `pwd`/$1
  check_files "^p[0-9][0-9][0-9][0-9][0-9]\.((bmp)|(jpg)|(jpeg)|(gif)|(png)|(tiff)|(orf))" "^v[0-9][0-9][0-9][0-9][0-9]\.((avi)|(mp4)|(mov)|(mpg)|(mpeg)|(wmv)|(3gp)|(flv)|(mts))" "^a[0-9][0-9][0-9][0-9][0-9]\.((mp3)|(wav))"
  check_folders
}

check_photos ()
{
   (cd "$1" ; check_files_folders "$1" ; cd "$REFERENCE_FOLDER")
}

REFERENCE_FOLDER=`pwd`
check_tools
check_usage "$@"
check_photos "$@"

