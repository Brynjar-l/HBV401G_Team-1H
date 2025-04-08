plugins {
    kotlin("jvm") version "2.1.10"
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "ice.private.brynj"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src/main/java", "src/main/kotlin"))
        resources.setSrcDirs(listOf("src/main/resources"))
    }
}






dependencies {
    testImplementation(kotlin("test"))

    /* Database Access */
    implementation("org.jetbrains.exposed:exposed-core:0.60.0")
    runtimeOnly("org.jetbrains.exposed:exposed-jdbc:0.60.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.60.0")

    /* DB Driver */
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")

    /* Logging */
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.4.14")


    // JavaFX
    implementation("org.openjfx:javafx-controls:21")
    implementation("org.openjfx:javafx-fxml:21")
    implementation("org.openjfx:javafx-media:21")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media")
}

application {
    mainClass.set("ice.private.brynj.MainKt")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}