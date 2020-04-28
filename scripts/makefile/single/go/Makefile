include ../scripts/makefile/single/Makefile

.PHONY: vendor-clean
vendor-clean:
	rm -rf vendor

.PHONY: vendor
vendor: vendor-clean
	go mod vendor

build-dev: build

build:
	go build main.go -o ${PROJECT_NAME}-${VERSION}

test:
	./scripts/single/go/go-test-coverage.sh ${PROJECT_NAME}

ci-build-dev: clean build-dev test

ci-build: clean build test