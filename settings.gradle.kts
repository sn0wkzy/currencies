rootProject.name = "currencies"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

rootDir.walkTopDown()
    .filter { it.isDirectory }
    .filter { dir -> dir.listFiles()?.any { it.name == "build.gradle.kts" } == true }
    .map { it.toRelativeString(rootDir).replace(File.separatorChar, ':') }
    .forEach { include(it) }

