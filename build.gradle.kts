import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.serialize)
    `maven-publish`
}

group = "org.mr0btain"
version = providers.gradleProperty("tmapplogger.version").get()

kotlin {
    // JDK 21 (Gradle downloads it through foojay when it isn't installed), the same as the apps
    jvmToolchain(21)

    android {
        namespace = "org.mr0btain.applogger"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // iOS only builds on a Mac (it needs Xcode), so elsewhere the targets are left out. Publishing every target
    // needs a Mac for this reason, see README.md
    if (System.getProperty("os.name").startsWith("Mac")) {
        iosArm64()
        iosSimulatorArm64()
    }

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.coroutines)
            api(libs.kotlinx.serialization.json)
            api(libs.kotlinx.datetime)
            api(libs.kotlinx.io)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

