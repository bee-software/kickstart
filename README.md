### Quick start

#### Preparing the database

You need a running database instance to run both the server and the test suites.

To start a ready to go Postgres instance with Docker, run:

```
docker run --name=kickstart-db -v $(pwd)/scripts/postgres:/docker-entrypoint-initdb.d -p5432:5432 -e POSTGRES_PASSWORD=secret -d postgres
```

Note : If you want to use you local Postgres instance make sure to run the script `scripts/postgres/create-databases.sql`
to create the local, test and acceptance databases.

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

You can now sign in with `guest/password`.

### Running on Docker

To build a docker image to your local docker daemon:

```shell
make docker-image
```

To run the previously built docker image, e.g.:
````shell
docker run -p 8080:8080 -e "SERVER_PORT=8080" -e "SERVER_HOST=0.0.0.0" -e "WWW_ROOT=/www" bee-software/kickstart 
````

You can now sign in with `guest/password`.


### Advanced Configuration

Configuration uses properties file named after the environment: 

* `etc/test.properties` for the test environment
* `etc/dev.properties` for the dev environment

Configuration can be overridden from the command line using environment variables.
For instance to run tests against an existing Postgres database running on port `54320`:

```shell
DB_PORT=54320 make run
```

To run the server on a different port, you can either set the `SERVER_PORT` environment variable, such as:

```shell
SERVER_PORT=8080 make run
```

or use the shortcut form:

```shell
make run PORT=8080
```

To run the build against a different environment, say `integration`, for which you will need to provide
an `etc/integration.properties` file on the classpath:

```shell
make build ENV=integration
```


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

Access the pipeline URL http://localhost:8080/teams/main/pipelines/kickstart or follow the build in your terminal.

For example, to follow the `test` job in the terminal:

````shell
fly -t kickstart watch -j kickstart/test
````
