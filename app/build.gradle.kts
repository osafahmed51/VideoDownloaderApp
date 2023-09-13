plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 33
    viewBinding.enable=true

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.github.smarteist:autoimageslider:1.4.0-appcompat")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    implementation ("com.mindorks.android:prdownloader:0.6.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("androidx.appcompat:appcompat:1.6.0-alpha04")
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation ("com.github.hotchemi:android-rate:1.0.1")
//    implementation ("com.arthenica:mobile-ffmpeg-full:4.4")
    implementation ("nl.bravobit:android-ffmpeg:1.1.7")
    implementation ("com.google.android.exoplayer:exoplayer-core:r2.4.0")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}