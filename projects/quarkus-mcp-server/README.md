# {{ appTitle }}

A Quarkus MCP (Model Context Protocol) server with jDeploy integration for desktop deployment.

## About

This project uses Quarkus, the Supersonic Subatomic Java Framework, to build an MCP server. It includes:

- **MCP Server Mode**: Run as a stdio-based MCP server for AI tool integration
- **Desktop GUI Mode**: Run as a Swing desktop application
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

### Production Mode

Build and run:

```bash
./gradlew buildUberJar
java -Djdeploy.mode=command -jar build/{{ artifactId }}-1.0.0-SNAPSHOT-runner.jar
```

### GUI Mode

Run in desktop GUI mode:

```bash
java -jar build/{{ artifactId }}-1.0.0-SNAPSHOT-runner.jar
```

## MCP Tools

This template includes example MCP tools:

- **greet**: Generate a greeting message for a person
- **echo**: Echo back a message, optionally in uppercase

Add your own tools by creating methods annotated with `@Tool` in a new class or in the existing `GreetingTools` class.

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

This project is configured for deployment via jDeploy. The MCP server is registered as an AI tool that can be used by AI assistants.

### Publishing

```bash
npm install -g jdeploy
jdeploy publish
```

## Related Guides

- [Quarkus MCP Server Guide](https://docs.quarkiverse.io/quarkus-mcp-server/dev/index.html)
- [Quarkus REST Client Guide](https://quarkus.io/guides/rest-client)
- [jDeploy Documentation](https://www.jdeploy.com/docs)
