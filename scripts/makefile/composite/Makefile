all: ci-build

.PHONY: setup
setup:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

.PHONY: clean
clean:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

.PHONY: build-dev
build-dev:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

.PHONY: build
build:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

.PHONY: test
test:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

.PHONY: ci-build
ci-build: 
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done