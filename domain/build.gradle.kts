plugins {
    id(libs.plugins.java.library.get().pluginId)
    id(libs.plugins.jetBrains.kotlin.jvm.get().pluginId)
    id(libs.plugins.jetBrains.kotlin.serialization.get().pluginId)
    alias(libs.plugins.agp.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
dependencies {
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(libs.koin.compiler)
    implementation(libs.coroutines.core)
    implementation(libs.paging.common)
    implementation(libs.jetBrains.kotlin.serialization)
}
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}