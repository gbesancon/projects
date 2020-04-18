#!/bin/bash
################################################################################
##  File:  migrate.sh
##  Desc:  This script automates the process of merging 2 other repos into one.
##         More details and reference: https://thoughts.t37.net/merging-2-different-git-repositories-without-losing-your-history-de7a06bba804
################################################################################

set -e
set -x
shopt -s extglob
shopt -s dotglob

migrate() {
    repo_name=$1
    folder_name=$2
    http_repository_url=$3

    # We clone in /tmp
    git clone $http_repository_url /tmp/$repo_name
    pushd /tmp/$repo_name
    # /tmp/$repo_name
    mkdir $folder_name
    git mv !(.|..|.git|$folder_name) $folder_name/
    git commit -a --no-edit -m "Migrating git related files from $repo_name to $folder_name"
    popd
    # Fetch code and history in repository
    git remote add $repo_name /tmp/$repo_name
    git fetch $repo_name
    git merge --allow-unrelated-histories $repo_name/master
    git remote rm $repo_name
    rm -rf /tmp/$repo_name/
    git clone $http_repository_url /tmp/$repo_name
    diff --exclude=.git -r ./$folder_name/ /tmp/$repo_name/
    rm -rf /tmp/$repo_name/
    echo $repo_name moved in folder $folder_name
}

REPOSITORY_NAMES="$@"
for REPOSITORY_NAME in $REPOSITORY_NAMES
do
    migrate $REPOSITORY_NAME $REPOSITORY_NAME "https://github.com/gbesancon/${REPOSITORY_NAME}.git"
done

exit 0
