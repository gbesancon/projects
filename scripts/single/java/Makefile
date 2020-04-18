include ../scripts/single/Makefile
VERSION=0.0.1-SNAPSHOT

clean:
	./gradlew clean -Pversion=${VERSION}

build-dev:
	./gradlew build report distribute -PuseMavenLocal -Pversion=${VERSION}

build:
	./gradlew build report distribute -Pversion=${VERSION}