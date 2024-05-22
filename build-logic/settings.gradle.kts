dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("library") { from(files("../gradle/library.versions.toml")) }
    }
}

rootProject.name = "build-logic"