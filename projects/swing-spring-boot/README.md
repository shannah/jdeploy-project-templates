# jDeploy Template Swing (Swing Boot Variant)

## About this Project

This project was generated using the "Swing Boot Starter" project template from [jDeploy](https://www.jdeploy.com), a tool to deploy Java desktop apps on Windows, Mac, and Linux.

To create your own application based on this template, open jDeploy, select "Create New Project", select the "Swing Boot Starter" template, and complete the wizard.

![Image](https://github.com/user-attachments/assets/eb4854db-facb-41c7-8491-d6a15466007a)

NOTE: This project is identical to the [Swing Java Maven Starter) template, except that this uses the Spring Boot Starter parent project to help with building the project.  This will build as an uber jar instead of bundling the dependencies in a lib directory.  This doesn't add any Spring dependencies to the app itself.  Spring is only used for the building of the app.

## Requirements

* Java 21

## Run
```
./mvnw spring-boot:run
```

## Build

```
./mvnw package
```

### Deploy

**Short Version:**
Open project in [jDeploy](https://www.jdeploy.com), and press "Publish"

[Long version](https://www.jdeploy.com/docs/manual/#_publishing_your_application)

### Advanced Deployment

See [jDeploy Manual](https://www.jdeploy.com/docs/manual/)
