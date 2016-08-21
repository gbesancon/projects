#!/bin/bash

check_tools ()
{
  command -v echo > /dev/null 2>&1 || { echo >&2 "echo is not installed.  Aborting."; exit 1; }
  command -v wget > /dev/null 2>&1 || { echo >&2 "wget is not installed.  Aborting."; exit 1; }
}

get_ansi2html ()
{
  echo fetch http://www.pixelbeat.org/scripts/ansi2html.sh
  rm -f ansi2html.sh
  wget http://www.pixelbeat.org/scripts/ansi2html.sh
}

check_tools
get_ansi2html

