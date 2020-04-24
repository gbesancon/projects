
docker-clean:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

docker-build-dev:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

docker-build:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

docker-publish:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

docker-run:
	docker stack deploy ${DOCKER_STACK_NAME} --compose-file docker-compose.yml

docker-stop:
	docker stack rm ${DOCKER_STACK_NAME}

k8s-connect:
	gcloud container clusters get-credentials ${ENVIRONMENT_NAME} --zone us-central1-a --project ${GCP_PROJECT_NAME}

k8s-clean: 
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done
	rm -rf k8s

k8s-prep-deploy:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
		if [ -f ./${project_name}/deployment-${VERSION}-${ENVIRONMENT_NAME}.yaml ]; then \
			mkdir -p k8s/${VERSION}/${ENVIRONMENT_NAME} \
			cp ./${project_name}/deployment-${VERSION}-${ENVIRONMENT_NAME}.yaml k8s/${VERSION}/${ENVIRONMENT_NAME}/deployment-${project_name}.yaml \
    	fi \
	done

k8s-deploy:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done