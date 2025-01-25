buildscript {
    val agp_version by extra("8.8.0")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
val navVersion: String by extra { "2.8.5" }
val lottieVersion: String by extra { "6.1.0" }
val intuit: String by extra { "1.1.1" }
val hiltVersion: String by extra { "2.55" }
val hiltCompilerVersion: String by extra { "1.2.0" }
val safeArgs: String by extra { "2.5.3" }

plugins {
    id("com.android.application") version "8.8.0" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("com.google.dagger.hilt.android") version "2.55" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    id("androidx.navigation.safeargs") version "2.8.5" apply false
}