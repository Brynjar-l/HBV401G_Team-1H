plugins {
    kotlin("jvm") version "2.1.10"
}

group = "ice.private.brynj"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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


}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}