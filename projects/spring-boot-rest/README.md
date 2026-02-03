# {{ appTitle }}

This project was generated using the jDeploy Spring Boot REST template.

## Requirements

- Java {{ javaVersion }} or later

## Run (Development)

```bash
./mvnw spring-boot:run
```

The REST endpoint will be available at `http://localhost:8080/hello`.

## Build

```bash
./mvnw package
```

## Deploy

Use the jDeploy application to publish this app as a native desktop application with service management.

Once installed, the service can be managed with:

```bash
{{ appName }} service start
{{ appName }} service stop
{{ appName }} service status
```

For advanced deployment options, see the [jDeploy manual](https://www.jdeploy.com/docs/manual/).
