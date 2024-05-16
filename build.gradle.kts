plugins {
    id("fabric-loom")
    kotlin("jvm")
    kotlin("plugin.serialization")
}

base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}

val fabricKotlinVersion: String by project
val javaVersion = JavaVersion.VERSION_21
val loaderVersion: String by project
val minecraftVersion: String by project

val modVersion: String by project
version = modVersion

val mavenGroup: String by project
group = mavenGroup

repositories {
    maven("https://api.modrinth.com/maven")
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    minecraft("com.mojang", "minecraft", minecraftVersion)

    val yarnMappings: String by project
    mappings("net.fabricmc", "yarn", yarnMappings, null, "v2")

    modImplementation("net.fabricmc", "fabric-loader", loaderVersion)

    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api", "fabric-api", fabricVersion)

    modImplementation("net.fabricmc", "fabric-language-kotlin", fabricKotlinVersion)

    include(modApi("teamreborn", "energy", "3.0.0"))

    val emiVersion: String by project
    modCompileOnly("dev.emi:emi-fabric:$emiVersion:api")
    modLocalRuntime("dev.emi", "emi-fabric", emiVersion)

    val jadeVersion: String by project
    modImplementation("maven.modrinth", "jade", jadeVersion)

    val reiVersion: String by project
    modLocalRuntime("me.shedaniel", "RoughlyEnoughItems-fabric", reiVersion)
    modCompileOnly("me.shedaniel", "RoughlyEnoughItems-api-fabric", reiVersion)
    modCompileOnly("me.shedaniel", "RoughlyEnoughItems-default-plugin-fabric", reiVersion)

    modApi("me.shedaniel.cloth:cloth-config-fabric:+") {
        exclude(group = "net.fabricmc.fabric-api")
    }

    include(modImplementation("maven.modrinth", "fstats", "2023.12.2"))

    include(modImplementation("maven.modrinth", "modmenu-badges-lib", "2023.6.1"))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = javaVersion.toString()
        }
    }

    jar {
        from("LICENSE")
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.version,
                    "loaderVersion" to loaderVersion,
                    "minecraftVersion" to minecraftVersion,
                    "fabricKotlinVersion" to fabricKotlinVersion,
                    "javaVersion" to javaVersion.toString()
                )
            )
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toString()))
        }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}
