#!/bin/bash

check_tools ()
{
  command -v echo > /dev/null 2>&1 || { echo >&2 "echo is not installed.  Aborting."; exit 1; }
  command -v cd > /dev/null 2>&1 || { echo >&2 "cd is not installed.  Aborting."; exit 1; }
  command -v pwd > /dev/null 2>&1 || { echo >&2 "pwd is not installed.  Aborting."; exit 1; }
  command -v tr > /dev/null 2>&1 || { echo >&2 "tr is not installed.  Aborting."; exit 1; }
  command -v printf > /dev/null 2>&1 || { echo >&2 "printf is not installed.  Aborting."; exit 1; }
  command -v mv > /dev/null 2>&1 || { echo >&2 "mv is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -gt 0 ]
    then
      for PHOTOS_FOLDER in "$@"; do
        if ! [[ -d "$PHOTOS_FOLDER" ]]; then
          echo "The photos folder $PHOTOS_FOLDER does not exist."
          exit 1
        fi
      done 
      echo "Start $0 :"
      echo " - Photos folders : " "$@"
    else
      echo "Usage: $0 PHOTOS_FOLDERS"
      exit 1
  fi
}

rename_files ()
{
  COUNTER=1
  for FILENAME in $1; do
    [ -f "$FILENAME" ] || continue # if not a file, skip
    EXTENSION=`echo "${FILENAME##*.}" | tr '[:upper:]' '[:lower:]'` # lowercase extension
    NEW_FILENAME="$2"`printf "%05d" $COUNTER`."$EXTENSION" # new filename
    #echo Rename $FILENAME $NEW_FILENAME
    mv "$FILENAME" "$NEW_FILENAME" > /dev/null  2> /dev/null
    COUNTER=`expr $COUNTER + 1`;
  done
}

rename_folders ()
{
  for FOLDER in *; do
    [ -d "$FOLDER" ] || continue # if not a directory, skip
    cd "$FOLDER"
    rename_files_folders "$FOLDER"
    cd ..
  done
}

rename_files_folders () 
{
  echo Processing `pwd`
  rename_files "*.bmp *.BMP *.jpg *.JPG *.jpeg *.JPEG *.gif *.GIF *.png *.PNG *.tiff *.TIFF *.orf *.ORF" "p"
  rename_files "*.avi *.AVI *.mp4 *.MP4 *.mov *.MOV *.mpg *.MPG *.mpeg *.MPEG *.wmv *.WMV *.3gp *.3GP *.flv *.FLV" "v"
  rename_files "*.mp3 *.MP3 *.wav *.WAV" "a"
  rename_folders
}

rename_photos ()
{
  for PHOTOS_FOLDER in "$@"; do 
    (cd "$PHOTOS_FOLDER" ; rename_files_folders ; cd "$REFERENCE_FOLDER")
  done
}

REFERENCE_FOLDER=`pwd`
check_tools
check_usage "$@"
rename_photos "$@"

