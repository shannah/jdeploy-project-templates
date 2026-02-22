# {{ appTitle }}

A JavaFX application with MCP (Model Context Protocol) server integration, deployed using jDeploy.

## Features

- **Dual-mode operation**: Run as a desktop GUI or as an MCP server for AI integration
- **JavaFX GUI**: Modern note-taking interface with search, create, and edit capabilities
- **MCP Tools**: AI assistants can list, create, search, and display notes
- **Background IPC**: Seamless communication between MCP server and GUI
- **Cross-platform**: Deploy to Windows, macOS, and Linux via jDeploy

## Requirements

- Java {{ javaVersion }} or higher
- Gradle (wrapper included)

## Building

```bash
# Build the uber-jar
./gradlew buildUberJar
```

## Running

### GUI Mode (default)
```bash
java -jar build/{{ artifactId }}-1.0.0-SNAPSHOT-runner.jar
```

### MCP Server Mode
```bash
java -Djdeploy.mode=command -jar build/{{ artifactId }}-1.0.0-SNAPSHOT-runner.jar
```

## MCP Integration

This application exposes the following MCP tools for AI assistants:

| Tool | Description |
|------|-------------|
| `listNotes` | List all notes with IDs and timestamps |
| `getNote` | Get the full content of a note by ID |
| `createNote` | Create a new note with title and content |
| `searchNotes` | Search notes by keyword |
| `showNote` | Navigate the GUI to display a specific note |
| `exportNote` | Export a note to a Markdown file |

## Project Structure

```
src/main/java/{{ packagePath }}/
├── {{ mainClass }}.java          # Main entry point (mode selection)
├── {{ mainClass }}App.java       # JavaFX GUI application
├── mcp/
│   └── {{ mainClass }}Tools.java # MCP tool definitions
└── model/
    ├── Note.java                 # Note data model
    └── NoteStore.java            # In-memory note storage
```

## Deployment

This project is configured for jDeploy distribution:

```bash
# Install jDeploy
npm install -g jdeploy

# Build and package
jdeploy install
```

## License

ISC
