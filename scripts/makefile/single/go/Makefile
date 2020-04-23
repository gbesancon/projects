include ../scripts/makefile/single/Makefile

.PHONY: vendor-clean
vendor-clean:
	rm -rf vendor

.PHONY: vendor
vendor: vendor-clean
	go mod vendor

test:
	./scripts/single/go/go-test-coverage.sh ${PROJECT_NAME}
