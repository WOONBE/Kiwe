import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.kiwe.kiosk.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kiwe.kiosk"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
    extensions.configure<DetektExtension> {
        toolVersion = "1.23.5"
        config.setFrom(files("${rootProject.projectDir}/config/detekt.yml"))
        buildUponDefaultConfig = true
        parallel = true
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            html.required.set(true)
            xml.required.set(true)
            txt.required.set(false)
            sarif.required.set(true)
        }
        jvmTarget = "11"
        ignoreFailures = false
    }

    tasks.withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":presentation:kiosk"))
    implementation(project(":domain"))
    implementation(project(":data"))
    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // detekt
    implementation(libs.detekt.gradle)
    detektPlugins(libs.detekt.formatting)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
}
