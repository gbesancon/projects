PROJECT_NAME=none
VERSION=0.0.0
ENVIRONMENT_NAME=localhost

clean:
	rm -rf deployment-*.yaml

build:
	source ../scripts/CONFIGURATION
	docker build --build-arg VERSION=${VERSION} -t ${DOCKER_IMAGE_PREFIX}/${PROJECT_NAME}:${VERSION} -f Dockerfile ..
	image_id=$(docker create ${DOCKER_IMAGE_PREFIX}/${PROJECT_NAME}:${VERSION})
	for file in "coberturaTestReport.xml" "TEST-${PROJECT_NAME}.xml"
	do
		docker cp ${image_id}:/app/build/test-results/${file} ./${file}
	done
	docker rm -v ${image_id}

build-dev: build

test:
	echo Test

docker-clean:
	source ../scripts/CONFIGURATION && \
	docker rmi -f ${DOCKER_IMAGE_PREFIX}/${PROJECT_NAME}:${VERSION}

docker-build: 
	echo Docker build

docker-build-dev: docker-build

docker-publish:
	source ../scripts/CONFIGURATION && \
	docker push ${DOCKER_IMAGE_PREFIX}/${PROJECT_NAME}:${VERSION}

k8s-clean:
	source ../scripts/CONFIGURATION && \
	kubectl delete configmap,service,deployment ${PROJECT_NAME} && \
	kubectl delete ingress ${PROJECT_NAME}-public ${PROJECT_NAME}-protected

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

k8s-deploy: k8s-prep-deploy
	kubectl apply -f deployment-${VERSION}-${ENVIRONMENT_NAME}.yaml

ci-build:
	echo CI build