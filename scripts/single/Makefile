# shellcheck disable=SC2046
PROJECT_NAME="$(basename $(dirname -z $(dirname -z $(readlink -f $0))))"
VERSION=0.0.1

all: ci-build

.PHONY: setup
setup:
	echo setup

.PHONY: clean
clean:
	echo clean

.PHONY: build
build:
	echo build

.PHONY: build-dev
build-dev:
	echo build-dev

.PHONY: test
test:
	echo test

.PHONY: ci-build
ci-build: setup clean build test