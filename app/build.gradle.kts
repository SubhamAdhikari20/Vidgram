plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-parcelize")

}

android {
    namespace = "com.example.vidgram"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.vidgram"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        viewBinding = true
    }
    buildToolsVersion = "35.0.0"
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.circleimageview)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.gridlayout)
    implementation(libs.androidx.runtime.android)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation (libs.glide)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.cloudinary.android)
    implementation(libs.picasso)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:+")    // add this line in your module-level build.gradle file's dependencies, usually named [app].

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
//    androidTestImplementation("org.mockito:mockito-kotlin:3.2.0")
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.espresso.intents)
    testImplementation(libs.mockito.kotlin) // ✅ Correct


}