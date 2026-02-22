package {{ packageName }}.mcp;

import ca.weblite.jdeploy.app.bridge.GuiBridge;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;

import java.util.Map;

/**
 * MCP tool definitions for the application.
 * <p>
 * Each method annotated with {@link Tool} becomes an MCP tool that AI assistants
 * (like Claude) can invoke. The {@link ToolArg} annotation describes each parameter.
 * <p>
 * These tools communicate with the JavaFX GUI via {@link GuiBridge}, which handles
 * the inter-process communication between the MCP server and the GUI.
 *
 * <h2>Example Usage (from an AI assistant)</h2>
 * <pre>
 * // List all notes
 * list_notes()
 *
 * // Create a new note
 * create_note(title="Meeting Notes", content="Discussed Q3 roadmap...")
 *
 * // Show a specific note in the GUI
 * show_note(id="abc123")
 * </pre>
 */
public class {{ mainClass }}Tools {

    /** Bridge for communicating with the GUI process. */
    private static final GuiBridge bridge = new GuiBridge("{{ appName }}");

    /**
     * Lists all notes in the application.
     * <p>
     * This is a read-only operation that doesn't affect the GUI.
     * Uses background IPC so the app stays in the background.
     *
     * @return JSON-formatted string containing notes with IDs, titles, and timestamps
     */
    @Tool(description = "List all notes in the application. "
            + "Returns note IDs, titles, and timestamps.")
    public String listNotes() throws Exception {
        Map<String, String> result = bridge.sendCommand("list_notes", Map.of());
        return formatResult(result);
    }

    /**
     * Retrieves the full content of a specific note.
     * <p>
     * Uses background IPC so the app stays in the background.
     *
     * @param id the unique identifier of the note
     * @return JSON-formatted string containing the note's full details including content
     */
    @Tool(description = "Get the full content of a note by its ID.")
    public String getNote(
            @ToolArg(description = "The note ID") String id) throws Exception {
        Map<String, String> result = bridge.sendCommand("get_note", Map.of("id", id));
        return formatResult(result);
    }

    /**
     * Creates a new note with the specified title and content.
     * <p>
     * The note is immediately saved and the GUI's note list is refreshed.
     * Uses background IPC so the app stays in the background.
     *
     * @param title   the title for the new note
     * @param content the body content for the new note
     * @return JSON-formatted string containing the created note's ID and metadata
     */
    @Tool(description = "Create a new note with the given title and content.")
    public String createNote(
            @ToolArg(description = "The note title") String title,
            @ToolArg(description = "The note content") String content) throws Exception {
        Map<String, String> result = bridge.sendCommand("create_note",
                Map.of("title", title, "content", content));
        return formatResult(result);
    }

    /**
     * Searches notes by keyword.
     * <p>
     * Searches both note titles and content. Uses background IPC.
     *
     * @param query the search term to look for
     * @return JSON-formatted string containing an array of matching notes
     */
    @Tool(description = "Search notes by keyword. Searches both titles and content.")
    public String searchNotes(
            @ToolArg(description = "The search query") String query) throws Exception {
        Map<String, String> result = bridge.sendCommand("search_notes", Map.of("query", query));
        return formatResult(result);
    }

    /**
     * Navigates the GUI to display a specific note.
     * <p>
     * This command brings the application window to the foreground and
     * selects the specified note. Uses deep link IPC because it's intended
     * to show something to the user.
     *
     * @param id the note ID to display
     * @return JSON-formatted string confirming the note was shown
     */
    @Tool(description = "Navigate the GUI to display a specific note. "
            + "Brings the window to the front.")
    public String showNote(
            @ToolArg(description = "The note ID to display") String id) throws Exception {
        // Use deep link because this command should bring the app to the foreground
        Map<String, String> result = bridge.sendCommandViaDeepLink("show_note", Map.of("id", id));
        return formatResult(result);
    }

    /**
     * Exports a note to a Markdown file.
     * <p>
     * Writes the note's title and content to the specified file path.
     * Uses background IPC so the app stays in the background.
     *
     * @param id   the note ID to export
     * @param path the file path to write to (e.g., "/tmp/note.md")
     * @return JSON-formatted string confirming the export with the output path
     */
    @Tool(description = "Export a note to a file. The note is written as Markdown.")
    public String exportNote(
            @ToolArg(description = "The note ID to export") String id,
            @ToolArg(description = "The file path to write to") String path) throws Exception {
        Map<String, String> result = bridge.sendCommand("export_note",
                Map.of("id", id, "path", path));
        return formatResult(result);
    }

    /**
     * Formats a result map as a simple JSON-like string for MCP response.
     */
    private static String formatResult(Map<String, String> result) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : result.entrySet()) {
            if (!first) sb.append(", ");
            first = false;
            sb.append("\"").append(entry.getKey()).append("\": ");
            sb.append("\"").append(escapeJson(entry.getValue())).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
