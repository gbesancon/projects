#!/bin/bash

check_usage ()
{
  if [ $# -neq 1 ] ; then
    echo "Usage: $0 YOUTUBE_URL"
    exit 1
  fi
}

check_usage "$@"
./youtube-mplayer.sh $1 caca

