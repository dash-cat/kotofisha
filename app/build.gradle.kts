plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.androidxNavigation)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.appDistribution)
    alias(libs.plugins.firebaseCrashlyticsGradle)
    alias(libs.plugins.perfPlaginFirebase)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "su.cus.spontanotalk"
    compileSdk = 34

    defaultConfig {
        applicationId = "su.cus.spontanotalk"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =  "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

//    packagingOptions {
//        resources.excludes.add("META-INF/*")
//    }
    buildToolsVersion = "34.0.0"
    ndkVersion = "26.1.10909125"

}

dependencies {
//    implementation(libs.kotlinxSerialization)
    implementation(libs.firebase.play.services.location)
    implementation(libs.firebase.ui.firestore)
    implementation(libs.firebase.ui.storage)
    implementation(libs.firebase.ui.auth)
    implementation(libs.firebase.ui.database)
    implementation(libs.firebase.perf) // This lib is fine, version controlled by BOM
    implementation(libs.firebase.analytics) // Corrected alias, version controlled by BOM
    implementation(platform(libs.firebase.boom))
//    implementation(libs.facebook)
    implementation(libs.kotlinx.serialization.json) // Updated alias
    implementation(libs.firebase.auth) // Corrected alias, version controlled by BOM
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.core.animation)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.media3.common)
    androidTestImplementation(libs.mokito.kotlin)
    androidTestImplementation(libs.android.test)
    androidTestImplementation(libs.mokito)
    androidTestImplementation(libs.unit)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata) // Changed from .livedata.ktx
    implementation(libs.androidx.lifecycle.viewmodel) // Changed from .viewmodel.ktx
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.org.robolectric)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.ext)
    androidTestImplementation(libs.androidx.espresso)
    implementation(libs.koin)
    implementation(libs.glide)
    implementation(libs.material)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    annotationProcessor(libs.glideCompiler)
    implementation(libs.kotlinx.serialization.json) // Updated alias
    implementation(libs.androidx.retrofit)
    implementation(libs.androidx.converter.gson)
    implementation(libs.androidx.recycler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime) // Changed from .runtime.ktx
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}