#!/bin/bash

check_tools ()
{
  command -v git > /dev/null 2>&1 || { echo >&2 "git is not installed.  Aborting."; exit 1; }
}

check_tools
if [ ! -d youtube-dl ] ; then
  git clone https://github.com/rg3/youtube-dl.git
  if [ ! -f youtube-dl ] ; then
    pushd youtube-dl
    make
    popd
  fi 
fi

