package {{ packageName }};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class {{ mainClass }} {

    public static void main(String[] args) {
        String mode = System.getProperty("jdeploy.mode", "gui");

        if ("gui".equals(mode)) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "{{ appTitle }} v1.0\n\nThis is an MCP server.\n"
                        + "Configure it in your AI tool's MCP settings.",
                    "About {{ appTitle }}",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
                System.exit(0);
            });
            return;
        }

        SpringApplication.run({{ mainClass }}.class, args);
    }
}
