@file:Suppress("DEPRECATION", "ObjectLiteralToLambda")

import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "io.github.achmadhafid.mathscanner"
    compileSdk = 33
    defaultConfig {
        applicationId = "io.github.achmadhafid.mathscanner"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    flavorDimensions += listOf("theme", "imageSource")
    productFlavors {
        create("red") {
            dimension = "theme"
            versionNameSuffix = "-red"
        }
        create("green") {
            dimension = "theme"
            versionNameSuffix = "-green"
        }
        create("filesystem") {
            dimension = "imageSource"
            versionNameSuffix = "-filesystem"
            buildConfigField("String", "IMAGE_SOURCE", "\"filesystem\"")
        }
        create("camera") {
            dimension = "imageSource"
            versionNameSuffix = "-built-in-camera"
            buildConfigField("String", "IMAGE_SOURCE", "\"camera\"")
        }
    }
    applicationVariants.all(object : Action<ApplicationVariant> {
        override fun execute(variant: ApplicationVariant) {
            variant.outputs.all(object : Action<BaseVariantOutput> {
                override fun execute(output: BaseVariantOutput) {
                    if (output is BaseVariantOutputImpl) {
                        output.outputFileName = "app-${variant.versionName}.apk"
                    }
                }
            })
        }
    })
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugarJdkLibs)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference)
    implementation(libs.material3)
    implementation(libs.lottie)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
