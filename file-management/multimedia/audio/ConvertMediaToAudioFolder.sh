#!/bin/bash

check_tools ()
{
  command -v avconv > /dev/null 2>&1 || { echo >&2 "avconv is not installed.  Aborting."; exit 1; }
  command -v mkdir > /dev/null 2>&1 || { echo >&2 "mkdir is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -eq 3 ]
    then
      if ! [[ -d "$1" ]]; then
        echo "Media folder $1 does not exist."
        exit 1
      fi
      if ! [[ -d "$2" ]]; then
        echo "Audio folder $2 does not exist."
        mkdir "$2"
      fi
      echo "Start $0 :"
      echo " - Media folder : $1"
      echo " - Audio folder : $2"
      echo " - Audio type   : $3"
    else
      echo "Usage: $0 MEDIA_FOLDER AUDIO_FOLDER AUDIO_TYPE"
      exit 1
  fi
}

convert_to_audio ()
{
  ./ConvertMediaToAudioFiles.sh "$1" "mp4" "$2" "$3"
  ./ConvertMediaToAudioFiles.sh "$1" "flv" "$2" "$3"
}

REFERENCE_FOLDER=`pwd`
check_tools
check_usage "$@"
convert_to_audio_file "$@"

