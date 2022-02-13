.PHONY: clean build all

VERSION := $(shell ./version.sh simple)

all: build docker-image

clean:
	@echo "Cleaning..."
	@./gradlew clean

test: ENV=test
test:
	@echo "Running tests of version ${VERSION}... in ${ENV} environment"
	@./gradlew test -Penv=${ENV}

acceptance: ENV=test
acceptance:
	@echo "Running acceptance tests of version ${VERSION} in ${ENV} environment"
	@./gradlew acceptanceTest -Penv=${ENV}

build: ENV=test
build:
	@echo "Building version ${VERSION}..."
	@./gradlew build -Penv=${ENV}

run: ENV=dev
run: PORT=8080
run:
	@echo "Starting server version ${VERSION}... in ${ENV} environment"
	@./gradlew run --args="-e ${ENV} -p ${PORT}"

docker-image:
	@echo "Building image ${VERSION}..."
	@./gradlew -Pversion=${VERSION} jibDockerBuild