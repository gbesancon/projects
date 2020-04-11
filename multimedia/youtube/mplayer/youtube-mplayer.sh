#!/bin/bash

check_tools ()
{
  pushd ../.. > /dev/null
  ./setup.sh
  popd
  command -v mplayer > /dev/null 2>&1 || { echo >&2 "mplayer is not installed.  Aborting."; exit 1; }
}

check_usage ()
{
  if [ $# -eq 1 ] ; then
      echo "Start $0 :"
      echo " - Youtube URL : $1"
    else
      if [ $# -eq 2 ] ; then
        echo "Start $0 :"
        echo " - Youtube URL : $1"
        echo " - Video Codec : $2"
      else
        echo "Usage: $0 YOUTUBE_URL"
        echo "Usage: $0 YOUTUBE_URL VIDEO_CODEC"
        exit 1
      fi
  fi
}

youtube-dl_mplayer ()
{
  pushd ../../youtube-dl
  if [ $# -eq 1 ] ; then
    ./youtube-dl $1 -o - | mplayer -
  else
    ./youtube-dl $1 -o - | mplayer -vo $2 -
  fi
  popd
}

check_tools
check_usage "$@"
youtube-dl_mplayer "$@"

