
docker-clean:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

docker-build-dev:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

docker-build:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

docker-publish:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

docker-run:
	docker stack deploy gbesancon --compose-file docker-compose.yml

docker-stop:
	docker stack rm gbesancon

k8s-connect:
	gcloud container clusters get-credentials ${ENVIRONMENT_NAME} --zone us-central1-a --project gbesancon

k8s-clean: 
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done
	rm -rf k8s

k8s-prep-deploy:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
		if [ -f ./${project}/deployment-${VERSION}-${ENVIRONMENT_NAME}.yaml ]; then \
			mkdir -p k8s/${VERSION}/${ENVIRONMENT_NAME} \
			cp ./${project}/deployment-${VERSION}-${ENVIRONMENT_NAME}.yaml k8s/${VERSION}/${ENVIRONMENT_NAME}/deployment-${project}.yaml \
    	fi \
	done

k8s-deploy:
	for project in ${PROJECTS} ; do \
		$(MAKE) -C $$project $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done