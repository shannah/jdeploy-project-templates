package {{ packageName }};

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolsConfig {

    @Tool(description = "Say hello to someone")
    public String greet(@ToolParam(description = "The name of the person to greet") String name) {
        return "Hello, " + name + "! Welcome to {{ appTitle }}.";
    }

    @Tool(description = "Echo back a message, optionally in uppercase")
    public String echo(
            @ToolParam(description = "The message to echo back") String message,
            @ToolParam(description = "Whether to convert the message to uppercase") boolean uppercase) {
        return uppercase ? message.toUpperCase() : message;
    }
}
