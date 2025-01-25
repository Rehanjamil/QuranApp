plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.quran.rabiulqaloob"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.quran.rabiulqaloob"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    val navVersion: String by rootProject.extra
    val lottieVersion: String by rootProject.extra
    val intuit: String by rootProject.extra
    val hiltVersion: String by rootProject.extra

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.paging:paging-runtime-ktx:3.3.5")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("androidx.browser:browser:1.8.0")
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.navigation:navigation-fragment-ktx:${navVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${navVersion}")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.airbnb.android:lottie:$lottieVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("androidx.work:work-runtime:2.10.0")
    implementation("com.intuit.ssp:ssp-android:$intuit")
    implementation("com.intuit.sdp:sdp-android:$intuit")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.akexorcist:localization:1.2.11")
    implementation("com.asksira.android:loopingviewpager:1.4.1")
    implementation("com.batoulapps.adhan:adhan:1.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
//    implementation("com.github.antonKozyriatskyi:CircularProgressIndicator:1.3.0")
}