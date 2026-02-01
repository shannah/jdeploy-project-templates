package {{ packageName }};

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import ca.weblite.jdeploy.app.JDeployOpenHandler;
import ca.weblite.jdeploy.app.swing.JDeploySwingApp;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.List;

@QuarkusMain
public class Main {

    public static final String MODE_GUI = "gui";
    public static final String PROP_MODE = "jdeploy.mode";

    public static void main(String... args) {
        // Check for Swing mode system property
        String mode = System.getProperty(PROP_MODE);

        if (MODE_GUI.equalsIgnoreCase(mode)) {
            // Launch Swing application
            launchSwingApp(args);
        } else {
            // Launch Quarkus normally
            Quarkus.run(args);
        }
    }

    private static void launchSwingApp(String... args) {
        System.out.println("Launching Swing application...");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("{{ appTitle }}");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());

            // Title label
            JLabel label = new JLabel("{{ appTitle }}", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Text area to display opened files/URLs
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setText("Waiting for files or URLs to be opened...\n");
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Opened Files/URLs"));

            frame.add(label, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Register handler for file and URL opening
            JDeploySwingApp.setOpenHandler(new JDeployOpenHandler() {
                @Override
                public void openFiles(List<File> files) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("FILES OPENED:\n");
                    for (File file : files) {
                        sb.append("  - ").append(file.getAbsolutePath()).append("\n");
                    }
                    textArea.append(sb.toString() + "\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                }

                @Override
                public void openURIs(List<URI> uris) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("URLS OPENED:\n");
                    for (URI uri : uris) {
                        sb.append("  - ").append(uri.toString()).append("\n");
                    }
                    textArea.append(sb.toString() + "\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                }

                @Override
                public void appActivated() {
                    textArea.append("APP ACTIVATED\n\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());

                    // Bring frame to front and request focus
                    frame.setState(JFrame.NORMAL);
                    frame.toFront();
                    frame.requestFocus();
                }
            });

            System.out.println("Swing mode activated with file/URL handling");
        });
    }
}
