# {{ appTitle }}

This project was generated using the jDeploy Spring Boot MCP Server template.

## Requirements

- Java {{ javaVersion }} or later

## Run (Development)

```bash
./mvnw spring-boot:run
```

The MCP server will start and communicate over stdin/stdout using the MCP protocol.

## Build

```bash
./mvnw package
```

## MCP Tools

This server provides the following MCP tools out of the box:

- **greet** - Say hello to someone
- **echo** - Echo back a message, optionally in uppercase

Add your own tools by creating methods annotated with `@Tool` in `McpToolsConfig.java` or any `@Configuration` class.

## Deploy

Use the jDeploy application to publish this app as a native desktop application with MCP server integration.

Once installed via jDeploy, AI tools like Claude Desktop, VS Code, and Cursor can automatically discover and use this MCP server.

For advanced deployment options, see the [jDeploy manual](https://www.jdeploy.com/docs/manual/).
