plugins {
    id(libs.plugins.java.library.get().pluginId)
    id(libs.plugins.jetBrains.kotlin.jvm.get().pluginId)
    id(libs.plugins.jetBrains.kotlin.serialization.get().pluginId)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
dependencies {
    api(libs.bundles.koin.core)
    ksp(libs.koin.compiler)
    implementation(libs.coroutines.core)
    implementation(libs.paging.common)
    implementation(libs.jetBrains.kotlin.serialization)
}
//kotlin {
//    sourceSets.main {
//        kotlin.srcDir("build/generated/ksp/main/kotlin")
//    }
//}