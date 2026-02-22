package {{ packageName }};

import ca.weblite.jdeploy.app.JDeployOpenHandler;
import ca.weblite.jdeploy.app.bridge.GuiBridgeHandler;
import ca.weblite.jdeploy.app.bridge.GuiBridgeReceiver;
import ca.weblite.jdeploy.app.javafx.JDeployFXApp;
import {{ packageName }}.model.Note;
import {{ packageName }}.model.NoteStore;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JavaFX application with MCP scripting support.
 * <p>
 * This application demonstrates how to make a JavaFX GUI scriptable via MCP,
 * similar to how AppleScript enables scripting of macOS applications.
 * <p>
 * The app uses {@link GuiBridgeReceiver} to receive commands from the MCP server
 * process and execute them in the GUI context.
 */
public class {{ mainClass }}App extends Application {

    private Stage primaryStage;
    private ListView<Note> noteListView;
    private ObservableList<Note> noteItems;
    private TextArea contentArea;
    private TextField titleField;
    private TextField searchField;
    private final NoteStore store = NoteStore.getInstance();

    /** Receiver for GUI bridge commands from MCP server. */
    private GuiBridgeReceiver bridgeReceiver;

    public static void main(String[] args) {
        // REQUIRED: Initialize JDeployFXApp before launching JavaFX
        // This registers AWT Desktop handlers while AWT is still the primary toolkit
        JDeployFXApp.initialize();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // --- Sidebar: note list + search + new button ---
        searchField = new TextField();
        searchField.setPromptText("Search notes...");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshList());

        noteItems = FXCollections.observableArrayList(store.getAllNotes());
        noteListView = new ListView<>(noteItems);
        noteListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Note note, boolean empty) {
                super.updateItem(note, empty);
                setText(empty || note == null ? null : note.getTitle());
            }
        });
        noteListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldNote, newNote) -> showNote(newNote));

        Button newButton = new Button("New Note");
        newButton.setMaxWidth(Double.MAX_VALUE);
        newButton.setOnAction(e -> createNewNote());

        VBox sidebar = new VBox(8, searchField, noteListView, newButton);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(250);
        VBox.setVgrow(noteListView, Priority.ALWAYS);

        // --- Editor: title + content ---
        titleField = new TextField();
        titleField.setPromptText("Note title");
        titleField.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        titleField.textProperty().addListener((obs, oldVal, newVal) -> {
            Note selected = noteListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setTitle(newVal);
                noteListView.refresh();
            }
        });

        contentArea = new TextArea();
        contentArea.setPromptText("Start writing...");
        contentArea.setWrapText(true);
        contentArea.textProperty().addListener((obs, oldVal, newVal) -> {
            Note selected = noteListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setContent(newVal);
            }
        });

        VBox editor = new VBox(8, titleField, contentArea);
        editor.setPadding(new Insets(10));
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        // --- Layout ---
        SplitPane root = new SplitPane(sidebar, editor);
        root.setOrientation(Orientation.HORIZONTAL);
        root.setDividerPositions(0.3);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("{{ appTitle }}");
        stage.setScene(scene);

        // Set up MCP command handling via GuiBridge
        setupBridgeReceiver();

        // Register deep link handler (for explicit "bring to front" scenarios)
        registerDeepLinkHandler();

        stage.show();

        // Select first note
        if (!noteItems.isEmpty()) {
            noteListView.getSelectionModel().select(0);
        }
    }

    // =========================================================================
    // GUI BRIDGE SETUP
    // =========================================================================

    /**
     * Sets up the GuiBridgeReceiver to handle commands from the MCP server.
     * <p>
     * This is the core integration point for MCP scripting. The handler
     * defines what commands the app supports and how they're executed.
     */
    private void setupBridgeReceiver() {
        // Create handler that processes MCP commands
        GuiBridgeHandler handler = this::handleCommand;

        // Create receiver and register for Hive messages (background IPC)
        bridgeReceiver = new GuiBridgeReceiver(handler);

        // Register with JavaFX thread dispatching for UI safety
        bridgeReceiver.registerHiveListener(requestPath ->
                Platform.runLater(() -> bridgeReceiver.processRequest(requestPath)));
    }

    /**
     * Handles a command from the MCP server.
     * <p>
     * This method defines the "scripting dictionary" for the app - the set of
     * commands that can be invoked via MCP.
     *
     * @param command the command name
     * @param params  the command parameters
     * @return the result as key-value pairs
     */
    private Map<String, String> handleCommand(String command, Map<String, String> params) {
        return switch (command) {
            case "list_notes" -> {
                Map<String, String> result = new HashMap<>();
                List<Note> notes = store.getAllNotes();
                result.put("count", String.valueOf(notes.size()));
                int i = 0;
                for (Note n : notes) {
                    result.put("note." + i + ".id", n.getId());
                    result.put("note." + i + ".title", n.getTitle());
                    result.put("note." + i + ".createdAt", n.getCreatedAt().toString());
                    result.put("note." + i + ".modifiedAt", n.getModifiedAt().toString());
                    i++;
                }
                yield result;
            }

            case "get_note" -> {
                String noteId = params.get("id");
                Note note = store.getNote(noteId)
                        .orElseThrow(() -> new RuntimeException("Note not found: " + noteId));
                yield Map.of(
                        "id", note.getId(),
                        "title", note.getTitle(),
                        "content", note.getContent(),
                        "createdAt", note.getCreatedAt().toString(),
                        "modifiedAt", note.getModifiedAt().toString()
                );
            }

            case "create_note" -> {
                String title = params.get("title");
                String content = params.getOrDefault("content", "");
                Note note = store.createNote(title, content);
                // Refresh the GUI list
                refreshList();
                yield Map.of(
                        "id", note.getId(),
                        "title", note.getTitle(),
                        "createdAt", note.getCreatedAt().toString(),
                        "modifiedAt", note.getModifiedAt().toString()
                );
            }

            case "search_notes" -> {
                String query = params.get("query");
                Map<String, String> result = new HashMap<>();
                List<Note> notes = store.search(query);
                result.put("count", String.valueOf(notes.size()));
                int i = 0;
                for (Note n : notes) {
                    result.put("note." + i + ".id", n.getId());
                    result.put("note." + i + ".title", n.getTitle());
                    i++;
                }
                yield result;
            }

            case "show_note" -> {
                String noteId = params.get("id");
                navigateToNote(noteId);
                yield Map.of("shown", "true");
            }

            case "export_note" -> {
                String noteId = params.get("id");
                String outputPath = params.get("path");
                Note note = store.getNote(noteId)
                        .orElseThrow(() -> new RuntimeException("Note not found: " + noteId));
                try {
                    Files.writeString(Path.of(outputPath),
                            "# " + note.getTitle() + "\n\n" + note.getContent());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to export: " + e.getMessage());
                }
                yield Map.of("exported", "true", "path", outputPath);
            }

            default -> throw new IllegalArgumentException("Unknown command: " + command);
        };
    }

    // =========================================================================
    // DEEP LINK HANDLER
    // =========================================================================

    /**
     * Registers handler for deep links (brings app to foreground).
     */
    private void registerDeepLinkHandler() {
        JDeployFXApp.setOpenHandler(new JDeployOpenHandler() {
            @Override
            public void openFiles(List<File> files) {
                // Not used in this app
            }

            @Override
            public void openURIs(List<URI> uris) {
                for (URI uri : uris) {
                    if ("{{ appName }}".equals(uri.getScheme()) && "bridge".equals(uri.getHost())) {
                        // Process via bridge receiver
                        bridgeReceiver.processDeepLink(uri);
                    }
                }
            }

            @Override
            public void appActivated() {
                primaryStage.setIconified(false);
                primaryStage.toFront();
                primaryStage.requestFocus();
            }
        });
    }

    // =========================================================================
    // NOTE DISPLAY
    // =========================================================================

    private void showNote(Note note) {
        if (note == null) {
            titleField.clear();
            contentArea.clear();
            return;
        }
        titleField.setText(note.getTitle());
        contentArea.setText(note.getContent());
    }

    private void createNewNote() {
        Note note = store.createNote("Untitled", "");
        refreshList();
        noteListView.getSelectionModel().select(note);
        titleField.requestFocus();
        titleField.selectAll();
    }

    /**
     * Refresh the note list, applying search filter if present.
     */
    public void refreshList() {
        String query = searchField.getText();
        List<Note> filtered = (query == null || query.isBlank())
                ? store.getAllNotes()
                : store.search(query);
        noteItems.setAll(filtered);
    }

    /**
     * Select and display a note by ID in the GUI.
     */
    public void navigateToNote(String noteId) {
        store.getNote(noteId).ifPresent(note -> {
            searchField.clear();
            refreshList();
            noteListView.getSelectionModel().select(note);
            noteListView.scrollTo(note);
            primaryStage.setIconified(false);
            primaryStage.toFront();
            primaryStage.requestFocus();
        });
    }
}
