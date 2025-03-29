package {{ packageName }}.services;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class WindowService {

    private static String computeScreensHash() {
        return Screen.getScreens().stream()
                .map(Screen::hashCode)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    private static void setInitialStagePosition(Stage stage) {

        Boolean screensAreSameAsLastTime = UserSettingsService.getScreensHash()
                .map(previousScreenHash -> Objects.equals(computeScreensHash(), previousScreenHash))
                .orElse(false);

        if (screensAreSameAsLastTime) {
            Optional<Double> windowX = UserSettingsService.getWindowX();
            Optional<Double> windowY = UserSettingsService.getWindowY();
            Optional<Double> windowWidth = UserSettingsService.getWindowWidth();
            Optional<Double> windowHeight = UserSettingsService.getWindowHeight();
            Optional<Boolean> windowMaximized = UserSettingsService.getWindowMaximized();

            if (windowX.isPresent() &&
                    windowY.isPresent() &&
                    windowWidth.isPresent() &&
                    windowHeight.isPresent() &&
                    windowMaximized.isPresent()) {
                stage.setX(windowX.get());
                stage.setY(windowY.get());
                stage.setWidth(windowWidth.get());
                stage.setHeight(windowHeight.get());
                stage.setMaximized(windowMaximized.get());
            }

        } else {
            stage.setMaximized(true);
        }

    }

    private static void closeWindowEvent(WindowEvent event, Stage stage) {

        Window window = (Window) event.getSource();

        String screens = computeScreensHash();

        UserSettingsService.setWindowPosition(
                window.getX(),
                window.getY(),
                window.getWidth(),
                window.getHeight(),
                stage.isMaximized(),
                screens);

        UserSettingsService.saveSettings();
    }

    public static void setup(Stage stage) {

        setInitialStagePosition(stage);

        stage.getScene().getWindow().addEventFilter(
                WindowEvent.WINDOW_CLOSE_REQUEST,
                event -> WindowService.closeWindowEvent(event, stage));

    }
}
