plugins {
    // Application
    id(libs.plugins.agp.application.get().pluginId)

    // Kotlin
    id(libs.plugins.jetBrains.kotlin.android.get().pluginId)

//    // Kapt
//    id(libs.plugins.jetBrains.kotlin.kapt.get().pluginId)

    // KSP
    alias(libs.plugins.agp.ksp)
}


android {

    namespace = misc.versions.appNamespace.get()
    compileSdk = config.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = misc.versions.appNamespace.get()
        minSdk = config.versions.minSdk.get().toInt()
        targetSdk = config.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = config.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName(config.versions.releaseBuildType.get()) {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        getByName(config.versions.debugBuildType.get()) {
            applicationIdSuffix = misc.versions.debugApplicationIdSuffix.get()
            isDebuggable = true
        }
        create("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = jvmOptions.versions.kotlinJvmTargetOptions.get()
    }

    //ViewBinding
    viewBinding.isEnabled = true
    applicationVariants.configureEach {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/${this@configureEach.name}/kotlin")
            }
        }

    }
//    applicationVariants.configureEach { variant ->
//        kotlin.sourceSets {
//            getByName(name) {
//                kotlin.srcDir("build/generated/ksp/${variant.name}/kotlin")
//            }
//        }
//    }
}

dependencies {
    implementation(project(misc.versions.domainProjectPath.get()))
    implementation(project(misc.versions.dataProjectPath.get()))

    // Profile Installer
    implementation(libs.android.profileInstaller)

    // UI Components
    implementation(libs.bundles.uiComponents)

    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.coroutines)

    // Navigation Component
    implementation(libs.bundles.navigation)

    // Koin
    implementation(libs.bundles.koin)
    implementation(libs.koin.annotations)
    ksp(libs.koin.compiler)
}
kotlin {
    sourceSets.getByName("benchmark") {
        kotlin.srcDir("build.generated/ksp/benchmark/kotlin")
    }
    sourceSets.debug {
        kotlin.srcDir("build/generated/ksp/debug/kotlin")
    }
    sourceSets.release {
        kotlin.srcDir("build/generated/ksp/release/kotlin")
    }
}