import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
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

dependencies {
    // detekt
    implementation(libs.detekt.gradle)
    detektPlugins(libs.detekt.formatting)
    // serialization
    implementation(libs.kotlinx.serialization.json)
}
