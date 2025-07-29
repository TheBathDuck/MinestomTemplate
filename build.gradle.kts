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
    maven(url = "https://repo.hypera.dev/snapshots/")
}

dependencies {
    implementation("net.minestom:minestom-snapshots:4fe2993057")

    implementation("net.kyori:adventure-text-minimessage:4.23.0")
    implementation("ch.qos.logback:logback-classic:1.5.18")

    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.spongepowered:configurate-core:4.1.2")

    implementation("dev.lu15:luckperms-minestom:5.4-SNAPSHOT") {
        exclude("org.spongepowered")
        exclude("net.minestom")
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "nl.thebathduck.minestom.Bootstrap"
    }
}