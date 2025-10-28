plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.drishtimukesh"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.drishtimukesh"
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
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
//    firebase
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.0")

    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
//    implementation(platform("com.google.firebase:firebase-bom:32.7.0")) // latest BOM
//    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")


    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("androidx.compose.foundation:foundation:1.5.0") // For HorizontalPager
    implementation("androidx.datastore:datastore-preferences:1.0.0") // For storing onboarding completion
    val nav_version = "2.8.9"
    implementation("androidx.navigation:navigation-compose:$nav_version") //navigation

    implementation("com.google.accompanist:accompanist-pager:0.32.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.32.0")

    implementation("io.coil-kt:coil-compose:2.6.0")//online image
    implementation("androidx.media3:media3-exoplayer:1.3.1")// exoplayer
    implementation("androidx.media3:media3-ui:1.3.1")

    implementation("com.razorpay:checkout:1.6.40") //razor pay
    implementation ("androidx.appcompat:appcompat:1.6.1") // or a newer stable version
    implementation ("com.google.code.gson:gson:2.10.1")
}