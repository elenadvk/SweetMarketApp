plugins {
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
    id("com.android.application")
}

android {

    namespace = "com.bitmobileedition.sweetmarket"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bitmobileedition.sweetmarket"
        minSdk = 28
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.glide)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.converter.scalars)  // If you're using the scalar converter
    implementation(libs.retrofit.v290)  // Retrofit dependency
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.glide.v4151)
    implementation(libs.androidx.room.ktx)
//    implementation(libs.firebase.crashlytics.buildtools)
//    implementation(libs.car.ui.lib)
//    implementation(libs.androidx.datastore.core.android)
//    implementation(libs.androidx.datastore.preferences.core.android)
//    implementation(libs.androidx.datastore.preferences.core.jvm)
//    implementation(libs.androidx.work.runtime.ktx)  // Add Glide
    annotationProcessor(libs.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.kotlinx.coroutines.android)
}