#!/bin/bash

check_tools ()
{
  command -v find > /dev/null 2>&1 || { echo >&2 "find is not installed.  Aborting."; exit 1; }
  command -v convert > /dev/null 2>&1 || { echo >&2 "convert is not installed.  Aborting."; exit 1; }
  command -v jpegoptim > /dev/null 2>&1 || { echo >&2 "jpegoptim is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -eq 2 ]
    then
      if ! [[ -d "$1" ]]; then
        echo "The photos top folder $1 does not exist."
        exit 1
      fi
      if ! [[ -d "$2" ]]; then
        echo "The thumbnails top folder $2 does not exist."
        exit 1
      fi
      echo "Start $0 :"
      echo " - Photos top folder : $1"
      echo " - Thumbnails top folder : $2"
    else
      echo "Usage: $0 PHOTOS_TOP_FOLDER THUMBNAILS_TOP_FOLDER"
      exit 1
  fi
}

create_thumbnails ()
{
  (cd "$1" ; find -type d -printf '%h/%f\n') |
  while read FOLDER; do # split other \n
    echo Processing "$2$FOLDER"
    mkdir "$2$FOLDER"
    (cd "$1$FOLDER" ; find -mindepth 1 -maxdepth 1 -type f \( -name "*.bmp" -or -name "*.jpg" -or -name "*.jpeg" -or -name "*.gif" -or -name "*.png" -or -name "*.tiff" \) -printf '%f\n') |
    while read FILE; do # split other \n
      rm -rf $2$FOLDER/$FILE 2> /dev/null
      #echo Create $3 thumbnails for $2$FOLDER/$FILE
      convert -auto-orient -resize $3 "$1$FOLDER/$FILE" "$2$FOLDER/$FILE" > /dev/null  2> /dev/null
      jpegoptim "$2$FOLDER/$FILE" > /dev/null 2> /dev/null
    done
  done
}

REFERENCE_FOLDER=`pwd`
check_tools
check_usage "$@"
create_thumbnails "$@" "800x600"


