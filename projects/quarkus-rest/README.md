# {{ appTitle }}

A Quarkus REST service with jDeploy integration for desktop deployment.

## About

This project uses Quarkus, the Supersonic Subatomic Java Framework. It includes:

- **REST API Mode**: Run as a standard REST service
- **Desktop GUI Mode**: Run as a Swing desktop application with custom URL scheme support
- **jDeploy Integration**: Package and distribute as native desktop installers

## Requirements

- Java {{ javaVersion }} or higher
- Gradle 9.x (wrapper included)

## Running the Application

### Development Mode

Run in dev mode with live coding:

```bash
./gradlew quarkusDev
```

The REST endpoint will be available at `http://localhost:8080/hello`

### Production Mode

Build and run:

```bash
./gradlew build
java -jar build/{{ artifactId }}-1.0.0-SNAPSHOT-runner.jar
```

### GUI Mode

Run in desktop GUI mode:

```bash
java -Djdeploy.mode=gui -jar build/{{ artifactId }}-1.0.0-SNAPSHOT-runner.jar
```

## Building an Uber JAR

To build a single JAR with all dependencies:

```bash
./gradlew buildUberJar
```

## Docker

### JVM Mode

```bash
./gradlew build
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/{{ artifactId }}-jvm .
docker run -i --rm -p 8080:8080 quarkus/{{ artifactId }}-jvm
```

### Native Mode

```bash
./gradlew build -Dquarkus.native.enabled=true
docker build -f src/main/docker/Dockerfile.native -t quarkus/{{ artifactId }} .
docker run -i --rm -p 8080:8080 quarkus/{{ artifactId }}
```

## jDeploy Deployment

This project is configured for deployment via jDeploy.

### Features

- **Singleton Mode**: Only one instance of the application runs at a time
- **Custom URL Scheme**: Register `{{ artifactId }}://` URLs to open in the app
- **Service Controller**: Implements updater and service controller interfaces

### Publishing

```bash
npm install -g jdeploy
jdeploy publish
```

## Related Guides

- [Quarkus REST Guide](https://quarkus.io/guides/rest)
- [jDeploy Documentation](https://www.jdeploy.com/docs)
