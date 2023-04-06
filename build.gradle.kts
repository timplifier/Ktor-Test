import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

plugins {
    alias(libs.plugins.agp.application) apply false
    alias(libs.plugins.agp.library) apply false
    alias(libs.plugins.jetBrains.kotlin.gradle) apply false
    alias(libs.plugins.jetBrains.kotlin.jvm) apply false
    alias(libs.plugins.jetBrains.kotlin.serialization) apply false
    alias(libs.plugins.navigation.safeArgs) apply false
    alias(libs.plugins.agp.ksp) apply false
}
allprojects {
    apply(plugin = rootProject.project.libs.plugins.agp.ksp.get().pluginId)

//    extensions.configure<KotlinJvmProjectExtension>("kotlin") {
//        sourceSets.getByName("main") {
//            kotlin.srcDir("build/generated/ksp/main/kotlin")
//        }
//    }
//
//    extensions.configure<KotlinAndroidProjectExtension>("kotlin") {
//        sourceSets.getByName("benchmark") {
//            kotlin.srcDir("build/generated/ksp/benchmark/kotlin")
//        }
//        sourceSets.getByName("debug") {
//            kotlin.srcDir("build/generated/ksp/debug/kotlin")
//        }
//        sourceSets.getByName("release") {
//            kotlin.srcDir("build/generated/ksp/release/kotlin")
//        }
//    }
}