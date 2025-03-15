plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.prm392_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.prm392_project"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.material:material:1.12.0")
    // RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // CardView
    implementation ("androidx.cardview:cardview:1.0.0")

    // Material Design
    implementation ("com.google.android.material:material:1.11.0")
    // AndroidX
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.drawerlayout:drawerlayout:1.2.0")
    // Navigation Component (nếu cần dùng Navigation Controller sau này)
    implementation ("androidx.navigation:navigation-fragment:2.7.7")
    implementation ("androidx.navigation:navigation-ui:2.7.7")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}