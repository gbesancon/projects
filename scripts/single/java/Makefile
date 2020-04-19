include ../scripts/single/Makefile
VERSION=0.0.1-SNAPSHOT

setup:
	bash ../scripts/single/java/install_jdk.sh oracle 14

clean:
	./gradlew clean -Pversion=${VERSION}

build-dev:
	./gradlew build report distribute -PuseMavenLocal -Pversion=${VERSION}

build:
	./gradlew build report distribute -Pversion=${VERSION}