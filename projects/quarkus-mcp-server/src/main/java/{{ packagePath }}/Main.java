package {{ packageName }};

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.swing.*;
import java.awt.*;

@QuarkusMain
public class Main {

    public static final String MODE_GUI = "gui";
    public static final String PROP_MODE = "jdeploy.mode";

    public static void main(String... args) {
        String mode = System.getProperty(PROP_MODE, MODE_GUI);

        if (MODE_GUI.equalsIgnoreCase(mode)) {
            launchSwingApp(args);
        } else {
            // Launch Quarkus MCP server
            Quarkus.run(args);
        }
    }

    private static void launchSwingApp(String... args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("{{ appTitle }}");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            frame.setLayout(new BorderLayout());

            // Title label
            JLabel titleLabel = new JLabel("{{ appTitle }}", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

            // Info panel with details about available tools
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

            JLabel statusLabel = new JLabel("Status: Running in GUI mode");
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel infoLabel = new JLabel("<html><center>This app provides tools via MCP.<br><br>" +
                    "Available tools:<br>" +
                    "- <b>greet</b>: Generate a greeting message<br>" +
                    "- <b>echo</b>: Echo back a message</center></html>");
            infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            infoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

            infoPanel.add(statusLabel);
            infoPanel.add(infoLabel);

            frame.add(titleLabel, BorderLayout.NORTH);
            frame.add(infoPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
