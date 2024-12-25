plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
    implementation(project(":core:domain"))

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.bundles.ktor)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
}