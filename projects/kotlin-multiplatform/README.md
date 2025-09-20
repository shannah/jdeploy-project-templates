This is a Kotlin Multiplatform project targeting Desktop (JVM).

![Image](https://github.com/user-attachments/assets/97153f2e-5f9c-49d9-8cb8-ef9349eae613)

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### Publishing with jDeploy

This project is pre-configured with [jDeploy](https://www.jdeploy.com) to automatically build native installers for Windows, macOS, and Linux. You can publish your application in two ways:

**Manual Publishing:**
- Use the [jDeploy CLI](https://github.com/shannah/jdeploy) or the [jDeploy Desktop App](https://www.jdeploy.com/download)
- Publish to npm registry or GitHub releases

**Automated Publishing:**
- GitHub Actions workflow is already configured in `.github/workflows/jdeploy.yml`
- Automatically builds native installers on every commit and release
- Creates platform-specific bundles (Windows .exe, macOS .dmg, Linux .deb/.rpm)

See an example release with native installers at: https://github.com/shannah/jdeploy-kmp-starter/releases/tag/v1.0.0

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…