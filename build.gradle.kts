plugins {
    id("java")
    id("java-library")
    id("com.diffplug.spotless") version ("6.25.0")
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

group = "com.github.sn0wkzy"
version = "1.0-SNAPSHOT"


allprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()

        maven(url = "https://jitpack.io")
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven(url = "https://repo.codemc.io/repository/maven-public/")
        maven(url = "https://oss.sonatype.org/content/groups/public/")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    spotless {
        java {
            removeUnusedImports()
            formatAnnotations()
            importOrder()
        }
    }
}
