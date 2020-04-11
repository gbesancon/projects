#!/bin/bash

check_tools ()
{
  command -v avconv > /dev/null 2>&1 || { echo >&2 "avconv is not installed.  Aborting."; exit 1; }
  command -v mkdir > /dev/null 2>&1 || { echo >&2 "mkdir is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -eq 4 ]
    then
      if ! [[ -d "$1" ]]; then
        echo "Media folder $1 does not exist."
        exit 1
      fi
      if ! [[ -d "$3" ]]; then
        echo "Audio folder $3 does not exist."
        mkdir "$3"
      fi
      echo "Start $0 :"
      echo " - Media folder : $1"
      echo " - Media filter : $2"
      echo " - Audio folder : $3"
      echo " - Audio type   : $4"
    else
      echo "Usage: $0 MEDIA_FOLDER MEDIA_FILTER AUDIO_FOLDER AUDIO_TYPE"
      exit 1
  fi
}

convert_to_audio ()
{
  (cd "$1" ; find -mindepth 1 -maxdepth 1 -type f \( -name "$2" \) -printf '%f\n') |
  while read FILENAME; do # split other \n
    EXTENSION=`echo "${FILENAME##*.}"`
    ./ConvertMediaToAudioFile.sh "$1/$FILENAME" "$3/${FILENAME%.$EXTENSION}.$4"
  done
}

REFERENCE_FOLDER=`pwd`
check_tools
check_usage "$@"
convert_to_audio "$@"

