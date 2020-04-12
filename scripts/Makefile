SHELL := /bin/bash

PROJECTS=42 amazon
VERSION=0.0.0
ENVIRONMENT_NAME=localhost

clean:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done
	rm -rf k8s

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

docker-clean:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

docker-build-dev:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

docker-build:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

docker-publish:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

k8s-connect:
	gcloud container clusters get-credentials ${ENVIRONMENT_NAME} --zone us-central1-a --project gbesancon

k8s-clean: 
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

k8s-prep-deploy:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
		if [ -f ./${project}/deployment-${VERSION}-${ENVIRONMENT_NAME}.yaml ]; then
			mkdir -p k8s/${VERSION}/${ENVIRONMENT_NAME}
			cp ./${project}/deployment-${VERSION}-${ENVIRONMENT_NAME}.yaml k8s/${VERSION}/${ENVIRONMENT_NAME}/deployment-${project}.yaml
    	fi
	done

k8s-deploy:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
	done

ci-build: