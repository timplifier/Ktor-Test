pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        create("config") {
            from(files("gradle/config.versions.toml"))
        }
        create("jvmOptions") {
            from(files("gradle/options.versions.toml"))
        }
        create("misc") {
            from(files("gradle/misc.versions.toml"))
        }
    }
}

rootProject.name = "KtorTest"

include(":app", ":data", ":domain")
include(":benchmark")