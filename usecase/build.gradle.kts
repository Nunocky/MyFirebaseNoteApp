plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.serialization)
}

android {
    namespace = "org.nunocky.myfirebasenoteapp"
    compileSdk = 36

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "org.nunocky.myfirebasenoteapp.HiltTestRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.dagger.hilt.android)
    implementation(libs.googleid)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.androidx.hilt.compiler)
    ksp(libs.dagger.hilt.android.compiler)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.firebase.firestore.ktx)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    // androidTestImplementation(libs.firebase.auth)
    androidTestImplementation(libs.firebase.auth.ktx)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockito.kotlin)
    kspAndroidTest(libs.dagger.hilt.android.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    // testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.turbine)
}