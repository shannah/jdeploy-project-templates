import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.shadowPlugin)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            // Include all desktop platforms for cross-platform compatibility
            implementation(compose.desktop.linux_x64)
            implementation(compose.desktop.linux_arm64)
            implementation(compose.desktop.macos_x64)
            implementation(compose.desktop.macos_arm64)
            implementation(compose.desktop.windows_x64)
            // Note: Windows ARM64 not yet supported in Compose Multiplatform 1.9.0
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "{{ packageName }}.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "{{ packageName }}"
            packageVersion = "1.0.0"
        }
    }
}

tasks.register<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJarExecutable") {
    archiveBaseName.set("{{ artifactId }}")
    archiveClassifier.set("all")
    from(kotlin.jvm().compilations.getByName("main").output)
    configurations = listOf(project.configurations.getByName("jvmRuntimeClasspath"))
    manifest {
        attributes["Main-Class"] = "{{ packageName }}.MainKt"
    }
}

tasks.register("buildExecutableJar") {
    dependsOn("shadowJarExecutable")
    doLast {
        println("Built executable JAR: composeApp/build/libs/{{ artifactId }}-all.jar")
    }
}
