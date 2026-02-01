package {{ packageName }};

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;

public class GreetingTools {

    @Tool(description = "Generate a greeting message for a person")
    String greet(@ToolArg(description = "The name of the person to greet") String name) {
        return "Hello, " + name + "! Welcome to {{ appTitle }}.";
    }

    @Tool(description = "Echo back a message, optionally in uppercase")
    String echo(
            @ToolArg(description = "The message to echo back") String message,
            @ToolArg(description = "Whether to convert the message to uppercase") boolean uppercase) {
        if (uppercase) {
            return message.toUpperCase();
        }
        return message;
    }
}
