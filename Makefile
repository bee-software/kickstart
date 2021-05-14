.PHONY: clean build all

VERSION := $(shell ./version.sh simple)

all: build docker-image

clean:
	@echo "Cleaning..."
	@./gradlew clean

build:
	@echo "Building version ${VERSION}..."
	@./gradlew build

run:
	@./gradlew run

docker-image:
	@echo "Building image ${VERSION}..."
	@./gradlew -Pversion=${VERSION} -p server jibDockerBuild