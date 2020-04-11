#!/bin/sh

GITHUB_REPOSITORY_BASEURL=https://github.com

get_github_repository_url()
{
  USERNAME=$1
  REPOSITORY_NAME=$2
  echo $GITHUB_REPOSITORY_BASEURL/$USERNAME/$REPOSITORY_NAME.git
}
