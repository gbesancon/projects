#!/bin/sh
set -e

git clone https://github.com/gbesancon/projects
git submodule init
git submodule sync
git submodule update -f
