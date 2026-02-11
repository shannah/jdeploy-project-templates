package {{ packageName }};

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "{{ appName }}",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "{{ appTitle }} - a picocli CLI application.")
public class {{ mainClass }} implements Callable<Integer> {

    @Parameters(index = "0", defaultValue = "World",
            description = "The name to greet (default: ${DEFAULT-VALUE}).")
    private String name;

    @Option(names = {"-c", "--count"}, defaultValue = "1",
            description = "Number of times to print the greeting (default: ${DEFAULT-VALUE}).")
    private int count;

    @Override
    public Integer call() {
        for (int i = 0; i < count; i++) {
            System.out.println("Hello, " + name + "!");
        }
        return 0;
    }

    public static void main(String[] args) {
        String mode = System.getProperty("jdeploy.mode", "command");

        if ("gui".equals(mode)) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "{{ appTitle }} v1.0\n\nThis is a command-line tool.\n"
                        + "Run '{{ appName }} --help' in a terminal for usage.",
                    "About {{ appTitle }}",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
                System.exit(0);
            });
            return;
        }

        int exitCode = new CommandLine(new {{ mainClass }}()).execute(args);
        System.exit(exitCode);
    }
}
