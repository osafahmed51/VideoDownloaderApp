import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id ("kotlin-android")
}


android {
    namespace = "com.example.myapplication"
    compileSdk = 34
    viewBinding.enable=true


    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 29
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
}

dependencies {

    val room_version = "2.5.2"
    val billing_version = "6.0.1"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.github.smarteist:autoimageslider:1.4.0-appcompat")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.mindorks.android:prdownloader:0.6.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("androidx.appcompat:appcompat:1.7.0-alpha03")
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation ("com.github.hotchemi:android-rate:1.0.1")
    implementation ("nl.bravobit:android-ffmpeg:1.1.7")
    implementation ("com.google.android.exoplayer:exoplayer-core:r2.4.0")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    kapt("androidx.lifecycle:lifecycle-compiler:2.6.2")
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    implementation ("com.google.android.gms:play-services-ads:22.4.0")
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    implementation("com.android.billingclient:billing:$billing_version")
    implementation("com.android.billingclient:billing-ktx:$billing_version")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}

