all: ci-build

clean:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

build-dev:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

build:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

test:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

ci-build: 
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done