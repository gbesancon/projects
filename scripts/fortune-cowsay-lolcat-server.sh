#!/bin/bash

check_usage ()
{
  if [ $# -eq 1 ]
    then
      if [ $1 -lt 0 ]; then
        echo "Port $1 is below 0."
        exit 1
      fi
      echo "Start $0 :"
      echo " - Port : $1"
    else
      echo "Usage: $0 PORT"
      exit 1
  fi
}

check_tools ()
{
  command -v echo > /dev/null 2>&1 || { echo >&2 "echo is not installed.  Aborting."; exit 1; }
  command -v wget > /dev/null 2>&1 || { echo >&2 "wget is not installed.  Aborting."; exit 1; }
  command -v /usr/games/fortune > /dev/null 2>&1 || { echo >&2 "fortune is not installed.  Aborting."; exit 1; }
  command -v /usr/games/cowsay > /dev/null 2>&1 || { echo >&2 "cowsay is not installed.  Aborting."; exit 1; }
  command -v /usr/games/lolcat > /dev/null 2>&1 || { echo >&2 "lolcat is not installed.  Aborting."; exit 1; }
  command -v nc > /dev/null 2>&1 || { echo >&2 "nc is not installed.  Aborting."; exit 1; }
}

run_server ()
{
  echo "http://localhost:$1/"
  while true; do { echo -e 'HTTP/1.1 200 OK\r\n'; /usr/games/fortune | /usr/games/cowsay -f $(ls /usr/share/cowsay/cows/ | shuf -n1) | /usr/games/lolcat -f | sh $(dirname $0)/ansi2html.sh --bg=dark; } | nc -l $1; done
}

check_usage "$@"
check_tools
run_server "$@"

