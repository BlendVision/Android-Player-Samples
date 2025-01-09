import java.util.Properties

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.blendvision.*")
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/blendvision/Android-Player-SDK")
            credentials {
                val properties = getLocalProperties()
                username = properties.getProperty("github.packages.user")
                password = properties.getProperty("github.packages.password")
            }
        }
    }
    versionCatalogs {
        create("library") { from(files("gradle/library.versions.toml")) }
    }
}

rootProject.name = "Android-Player-Samples"
include(":BasicPlayback")
include(":BasicAnalysis")
include(":BasicAudio")
include(":BasicCasting")
include(":BasicDownload")
include(":BasicTVPlayback")
include(":AdvancedPlayback")
include(":ULLPlayback")
include(":PSEPlayback")

fun getLocalProperties(): Properties {
    val properties = Properties()
    val localPropertiesFile = File(rootDir, "local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { properties.load(it) }
    }
    return properties
}



 