include ../scripts/single/Makefile
VERSION=0.0.1-SNAPSHOT

.PHONY: vendor-clean
vendor-clean:
	rm -rf vendor

.PHONY: vendor
vendor: vendor-clean
	go mod vendor

test:
	./scripts/single/go/go-test-coverage.sh ${PROJECT_NAME}
