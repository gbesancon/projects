include ../scripts/makefile/single/Makefile
VERSION=0.0.1-SNAPSHOT
JAVA_VERSION=14

setup:
	bash ../scripts/makefile/single/java/install_jdk.sh oracle ${JAVA_VERSION}

clean:
	# https://hub.docker.com/_/openjdk/
	sudo docker run --rm -v `pwd`:/usr/src/${PROJECT_NAME} -v `pwd`/../scripts:/usr/src/scripts -w /usr/src/${PROJECT_NAME} openjdk:${JAVA_VERSION} ./gradlew clean -Pversion=${VERSION}

build-dev:
	# https://hub.docker.com/_/openjdk/
	sudo docker run --rm -v `pwd`:/usr/src/${PROJECT_NAME} -v `pwd`/../scripts:/usr/src/scripts -w /usr/src/${PROJECT_NAME} openjdk:${JAVA_VERSION} ./gradlew build report -PuseMavenLocal -Pversion=${VERSION}

build:
	# https://hub.docker.com/_/openjdk/
	sudo docker run --rm -v `pwd`:/usr/src/${PROJECT_NAME} -v `pwd`/../scripts:/usr/src/scripts -w /usr/src/${PROJECT_NAME} openjdk:${JAVA_VERSION} ./gradlew build report -Pversion=${VERSION}

publish-dev:
	# https://hub.docker.com/_/openjdk/
	sudo docker run --rm -v `pwd`:/usr/src/${PROJECT_NAME} -v `pwd`/../scripts:/usr/src/scripts -w /usr/src/${PROJECT_NAME} openjdk:${JAVA_VERSION} ./gradlew distribute -PuseMavenLocal -Pversion=${VERSION}

publish:
	# https://hub.docker.com/_/openjdk/
	sudo docker run --rm -v `pwd`:/usr/src/${PROJECT_NAME} -v `pwd`/../scripts:/usr/src/scripts -w /usr/src/${PROJECT_NAME} openjdk:${JAVA_VERSION} ./gradlew distribute -Pversion=${VERSION}

ci-build-dev:
	# https://hub.docker.com/_/openjdk/
	sudo docker run --rm -v `pwd`:/usr/src/${PROJECT_NAME} -v `pwd`/../scripts:/usr/src/scripts -w /usr/src/${PROJECT_NAME} openjdk:${JAVA_VERSION} ./gradlew clean build report distribute -PuseMavenLocal -Pversion=${VERSION}

ci-build:
	# https://hub.docker.com/_/openjdk/
	sudo docker run --rm -v `pwd`:/usr/src/${PROJECT_NAME} -v `pwd`/../scripts:/usr/src/scripts -w /usr/src/${PROJECT_NAME} openjdk:${JAVA_VERSION} ./gradlew clean build report distribute -Pversion=${VERSION}
