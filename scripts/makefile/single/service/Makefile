include ./project.Makefile

DOCKER_IMAGE_PREFIX=gbesancon
ENVIRONMENT_NAME=localhost

clean:
	rm -rf deployment-*.yaml

.PHONY: docker-clean
docker-clean:
	source ../scripts/CONFIGURATION && \
	docker rmi -f ${DOCKER_IMAGE_PREFIX}/${PROJECT_NAME}:${VERSION}

.PHONY: docker-build
docker-build: 
	source ../scripts/CONFIGURATION && \\
	docker build --build-arg VERSION=${VERSION} -t ${DOCKER_IMAGE_PREFIX}/${PROJECT_NAME}:${VERSION} -f Dockerfile .. && \\
	image_id=$(docker create ${DOCKER_IMAGE_PREFIX}/${PROJECT_NAME}:${VERSION}) && \
	for file in "coberturaTestReport.xml" "TEST-${PROJECT_NAME}.xml" \
	do \
		docker cp ${image_id}:/app/build/test-results/${file} ./${file} \
	done
	docker rm -v ${image_id}

.PHONY: docker-build-dev
docker-build-dev: docker-build

.PHONY: docker-publish
docker-publish:
	source ../scripts/CONFIGURATION && \
	docker push ${DOCKER_IMAGE_PREFIX}/${PROJECT_NAME}:${VERSION}

.PHONY: k8s-clean
k8s-clean:
	source ../scripts/CONFIGURATION && \
	kubectl delete configmap,service,deployment ${PROJECT_NAME} && \
	kubectl delete ingress ${PROJECT_NAME}-public ${PROJECT_NAME}-protected

.PHONY: k8s-prep-deploy
k8s-prep-deploy:
	source scripts/env/env.${ENVIRONMENT_NAME}.sh && \
	cat deployment.yaml \
		| sed "s#\${DOCKER_IMAGE_PREFIX}#${DOCKER_IMAGE_PREFIX}#g" \
		| sed "s#\${PROJECT_NAME}#${PROJECT_NAME}#g" \
		| sed "s#\${VERSION}#${VERSION}#g" \
		| sed "s#\${ENVIRONMENT_NAME}#${ENVIRONMENT_NAME}#g" \
		| sed "s#\${CNAME}#${CNAME}#g" \
		| sed "s#\${IMAGE_PULL_POLICY}#${IMAGE_PULL_POLICY}#g" \
		> deployment-${VERSION}-${ENVIRONMENT_NAME}.yaml

.PHONY: k8s-deploy
k8s-deploy: k8s-prep-deploy
	kubectl apply -f deployment-${VERSION}-${ENVIRONMENT_NAME}.yaml

ci-build: clean build test docker-build docker-publish