#!/bin/bash

check_tools ()
{
  command -v avconv > /dev/null 2>&1 || { echo >&2 "avconv is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -eq 2 ]
    then
      if ! [[ -f "$1" ]]; then
        echo "Media file $1 does not exist."
        exit 1
      fi
      echo "Start $0 :"
      echo " - Media file : $1"
      echo " - Audio file : $2"
    else
      echo "Usage: $0 MEDIA_FILE AUDIO_FILE"
      exit 1
  fi
}

convert_to_mp3 ()
{
  echo "MP3 convertion"
  avconv -y -i "$1" -acodec libmp3lame -ab 160k -ac 2 -ar 44100 "$2"
}

convert_to_ogg ()
{
  echo "OGG convertion"
  avconv -y -i "$1" -acodec libvorbis -aq 3 -vn -ac 2 "$2"
}

convert_to_audio ()
{
  EXTENSION=`echo "${2##*.}"`
  if [[ $EXTENSION =~ "mp3" ]]; then
    convert_to_mp3 "$@"
  else
    if [[ $EXTENSION =~ "ogg" ]]; then
      convert_to_ogg "$@"
    else
      echo "$EXTENSION not supported"      
    fi
  fi
}

REFERENCE_FOLDER=`pwd`
check_tools
check_usage "$@"
convert_to_audio "$@"

