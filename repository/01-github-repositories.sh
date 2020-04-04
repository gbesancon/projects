#!/bin/sh

source $(dirname $(realpath ${BASH_SOURCE[0]}))/../linux/utils-github.sh
source $(dirname $(realpath ${BASH_SOURCE[0]}))/../linux/utils-git.sh

REPOSITORY_NAMES="42 amazon bbingo build-gradle-scripts capgemini-fruitshop cheatsheet codingame codurance cv daily-coding-problem database-dependencies eclipse-utility file-management 
fortune-cowsay-lolcat-server jpmorgan-super-simple-stocks multimedia pi speakerphat visualstudio-projectdependencies"
for REPOSITORY_NAME in $REPOSITORY_NAMES
do
  git_clone_repository $(get_github_repository_url gbesancon $REPOSITORY_NAME) $REPOSITORY_NAME
  exit_on_error $?
done