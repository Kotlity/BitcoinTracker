plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.kotlity.feature_transactions"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:resources"))
    implementation(project(":core:presentation"))
    implementation(project(":core:database"))

    implementation(libs.bundles.android.core)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(libs.bundles.paging)

    implementation(libs.bundles.navigation)

    implementation(libs.threeTenabp)

    debugImplementation(libs.bundles.compose.debug)

    testImplementation(platform(libs.koin.bom))
    testImplementation(libs.bundles.test)

    androidTestImplementation(platform(libs.koin.bom))
    androidTestImplementation(libs.bundles.android.test)
}