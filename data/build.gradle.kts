import com.android.build.api.variant.BuildConfigField

plugins {
    // Application
    id(libs.plugins.agp.library.get().pluginId)

    // Kotlin
    id(libs.plugins.jetBrains.kotlin.android.get().pluginId)
}

android {

    namespace = misc.versions.dataNamespace.get()
    compileSdk = config.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = config.versions.minSdk.get().toInt()
        targetSdk = config.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(config.versions.releaseBuildType.get()) {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = jvmOptions.versions.kotlinJvmTargetOptions.get()
    }
}
androidComponents {
    onVariants {
        it.buildConfigFields.put(
            "BASE_URL", BuildConfigField(
                "String", "\"" + System.currentTimeMillis().toString() + "\"", "build timestamp"
            )
        )
    }
}

dependencies {
    implementation(project(misc.versions.domainProjectPath.get()))

    // Ktor
    implementation(libs.bundles.ktor)
    implementation(libs.jetBrains.ktor.engine.okHttp)
    implementation(libs.jetBrains.ktor.serialization.gson)
    implementation(libs.jetBrains.ktor.serialization.json)

    // OkHttp
    implementation(libs.bundles.okHttp)

    // Retrofit
    implementation(libs.bundles.retrofit)

    // Koin
    ksp(libs.koin.compiler)

    // Coroutines Core
    implementation(libs.coroutines.core)

    // Paging
    api(libs.paging.paging)
}
kotlin {
    sourceSets.debug {
        kotlin.srcDir("build/generated/ksp/debug/kotlin")
    }
    sourceSets.release {
        kotlin.srcDir("build/generated/ksp/release/kotlin")
    }
}