include ../scripts/single/Makefile
VERSION=0.0.1-SNAPSHOT

vendor-clean:
	rm -rf vendor

vendor: vendor-clean
	go mod vendor

test:
	./scripts/single/go/go-test-coverage.sh ${PROJECT_NAME}
