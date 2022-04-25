import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("fabric-loom") version "0.12.19"
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
    id("io.freefair.lombok") version "6.4.3"
}

val modVersion = project.properties["mod_version"].toString()
val modName = project.properties["mod_name"].toString()
val group = project.properties["maven_group"].toString()
val yarnMappings = project.properties["yarn_mappings"].toString()
val minecraftVersion = project.properties["minecraft_version"].toString()
val fabricLoaderVersion = project.properties["loader_version"].toString()
val fabricVersion = project.properties["fabric_version"].toString()
val fabricKotlinVersion = project.properties["fabric_kotlin_version"].toString()

version = modVersion

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    mappings("net.fabricmc:yarn:${yarnMappings}:v2")
    modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
//    modImplementation(group = "net.fabricmc", name = "fabric-language-kotlin", version = fabricKotlinVersion)

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabricVersion}")

    shadow("org.luaj:luaj-jse:3.0.1")
    shadow("com.gitlab.theoparis:event-bus:main-SNAPSHOT")
}


tasks {
    shadowJar {
        archiveBaseName.set(modName)
        archiveVersion.set(modVersion)

        configurations = listOf(
            project.configurations.named("shadow").get()
        )

        dependencies {
            exclude("META-INF")
            exclude(dependency("org.lwjgl:lwjgl"))
            exclude(dependency("org.lwjgl:lwjgl-glfw"))
            exclude(dependency("org.lwjgl:lwjgl-opengl"))
        }
    }
}

tasks {
    remapJar {
        dependsOn(project.tasks.getByName("shadowJar"))
        inputFile.set(named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
    }
}
