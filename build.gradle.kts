plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.4"
}

group = "nl.thebathduck.minestom"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://jitpack.io")
}

dependencies {
    // get latest from https://minestom.net/docs/setup/dependencies
    implementation("net.minestom:minestom-snapshots:1f34e60ea6")

    implementation("net.kyori:adventure-text-minimessage:4.12.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "nl.thebathduck.minestom.Bootstrap"
    }
}