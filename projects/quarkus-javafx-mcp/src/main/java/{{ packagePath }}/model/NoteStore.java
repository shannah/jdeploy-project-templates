package {{ packageName }}.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Singleton store for managing notes.
 * <p>
 * This is a simple in-memory store. In a real application, you would
 * likely persist notes to a database or file system.
 */
public class NoteStore {
    private static final NoteStore INSTANCE = new NoteStore();
    private final List<Note> notes = new ArrayList<>();

    public static NoteStore getInstance() { return INSTANCE; }

    private NoteStore() {
        // Start with a sample note
        notes.add(new Note("Welcome", "Welcome to {{ appTitle }}! "
                + "This app can be scripted by AI agents via MCP."));
    }

    public synchronized List<Note> getAllNotes() {
        return new ArrayList<>(notes);
    }

    public synchronized Optional<Note> getNote(String id) {
        return notes.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }

    public synchronized Note createNote(String title, String content) {
        Note note = new Note(title, content);
        notes.add(note);
        return note;
    }

    public synchronized boolean deleteNote(String id) {
        return notes.removeIf(n -> n.getId().equals(id));
    }

    public synchronized List<Note> search(String query) {
        String lower = query.toLowerCase();
        return notes.stream()
                .filter(n -> n.getTitle().toLowerCase().contains(lower)
                        || n.getContent().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
}
