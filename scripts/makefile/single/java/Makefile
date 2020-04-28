include ../scripts/makefile/single/Makefile
VERSION=0.0.1-SNAPSHOT
JAVA_VERSION=14

# https://hub.docker.com/_/openjdk/
DOCKER_RUN_GRADLE_COMMAND=sudo docker run --rm -v `pwd`:/usr/src/${PROJECT_NAME} -v `pwd`/../scripts:/usr/src/scripts -w /usr/src/${PROJECT_NAME} -e AZURE_DEVOPS_PACKAGE_TOKEN=${AZURE_DEVOPS_PACKAGE_TOKEN} openjdk:${JAVA_VERSION} ./gradlew -Pversion=${VERSION}

setup:
	bash ../scripts/makefile/single/java/install_jdk.sh oracle ${JAVA_VERSION}

clean:
	${DOCKER_RUN_GRADLE_COMMAND} clean

build-dev:
	${DOCKER_RUN_GRADLE_COMMAND} build report -PuseMavenLocal

build:
	${DOCKER_RUN_GRADLE_COMMAND} build report

publish-dev:
	${DOCKER_RUN_GRADLE_COMMAND} distribute -PuseMavenLocal

publish:
	${DOCKER_RUN_GRADLE_COMMAND} distribute

ci-build-dev:
	${DOCKER_RUN_GRADLE_COMMAND} clean build report distribute -PuseMavenLocal

ci-build:
	${DOCKER_RUN_GRADLE_COMMAND} clean build report distribute
