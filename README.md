### Quick start

Build the application with:

```shell
make build 
```

Run with:

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



