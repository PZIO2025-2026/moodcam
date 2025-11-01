plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.moodcam.frontend_android"
    compileSdk = 36

    buildFeatures {
        compose = true
        mlModelBinding = true
    }

    defaultConfig {
        applicationId = "com.moodcam.frontend_android"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.scenecore)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.runtime.livedata)

    // Splash Screen API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Changed from play-services-mlkit-face-detection to bundled face-detection
    implementation("com.google.mlkit:face-detection:16.1.7")


    val composeBom = platform("androidx.compose:compose-bom:2025.01.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Jetpack compose
    implementation("androidx.activity:activity-compose:1.9.0")

    // Material Design 3
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-extended-android:1.6.8")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

    // CameraX
    val cameraxVersion = "1.5.1"
    implementation("androidx.camera:camera-core:${cameraxVersion}")
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")

    // OpenCV
    implementation("org.opencv:opencv:4.9.0")

    // Accompanist for Permissions
    val accompanistVersion = "0.34.0"
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-analytics")

    // Add the dependencies for Firebase Authentication and Cloud Firestore
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // TF Lite
    implementation("org.tensorflow:tensorflow-lite:2.17.0")

    // Dependency injection
    val koinVersion = "3.5.3"
    implementation("io.insert-koin:koin-android:${koinVersion}")
    implementation("io.insert-koin:koin-androidx-compose:${koinVersion}")

    // Vico charts
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)
}