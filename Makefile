.PHONY: clean build all

VERSION := $(shell ./version.sh simple)

all: build docker-image

clean:
	@echo "Cleaning..."
	@./gradlew clean

test:
	@echo "Running tests of version ${VERSION}..."
	@./gradlew test

acceptance:
	@echo "Running acceptance tests of version ${VERSION}..."
	@./gradlew acceptanceTest

build:
	@echo "Building version ${VERSION}..."
	@./gradlew build

run:
	@echo "Starting server version ${VERSION}..."
	@./gradlew run

docker-image:
	@echo "Building image ${VERSION}..."
	@./gradlew -Pversion=${VERSION} jibDockerBuild