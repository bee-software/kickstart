### Quick start

#### Preparing the database

Note: _You need a running database instance to run both the server and the test suites._

To start a ready to go Postgres instance with Docker, run:

```
docker run --name=kickstart-db -v $(pwd)/scripts/postgres:/docker-entrypoint-initdb.d -p5432:5432 -e POSTGRES_PASSWORD=secret -d postgres
```

#### Building the application

Build using Gradle with:

```shell
make build 
```

#### Starting the application

Start the server from Gradle with:

```shell
make run
```

### Running on Docker

To build a docker image to your local docker daemon:

```shell
make docker-image
```

To run the previously built docker image, e.g.:
````shell
docker run -p 8080:8080 -e "SERVER_PORT=8080" -e "SERVER_HOST=0.0.0.0" -e "WWW_ROOT=/www" bee-software/kickstart 
````


### Running Concourse

#### Starting Concourse

To start Concourse, the CI server, run:

````shell
docker compose -f concourse.yml up -d
````

This will run the default Concourse docker image. Open http://127.0.0.1:8080/ to access Concourse in your browser.

Download the fly CLI or install it via Homebrew, then set up the Concourse target:

````shell
fly --target kickstart login --concourse-url http://127.0.0.1:8080 -u test -p test
fly --target kickstart sync
````

#### Setting up the build pipeline

Install the build pipeline in concourse by running:

````shell
fly -t kickstart set-pipeline -c scripts/ci/pipeline.yml -p kickstart
````

Unpause the newly created pipeline and job:

````shell
fly -t kickstart unpause-pipeline -p kickstart
fly -t kickstart unpause-job --job kickstart/test
````

Access the pipeline URL http://localhost:8080/teams/main/pipelines/kickstart or follow the build in your terminal
with, e.g.:

````shell
fly -t kickstart watch -j kickstart/test
````
