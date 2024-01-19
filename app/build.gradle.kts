plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.androidxNavigation)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.appDistribution)
    alias(libs.plugins.firebaseCrashlyticsGradle)
    alias(libs.plugins.perfPlaginFirebase)
    alias(libs.plugins.googleServices)
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
        kotlinCompilerExtensionVersion = "1.5.8"
    }

//    packagingOptions {
//        resources.excludes.add("META-INF/*")
//    }
    buildToolsVersion = "34.0.0"
    ndkVersion = "26.1.10909125"

}

dependencies {
    implementation(libs.firebase.play.services.location)
    implementation(libs.firebase.ui.firestore)
    implementation(libs.firebase.ui.storage)
    implementation(libs.firebase.ui.auth)
    implementation(libs.firebase.ui.database)
//    implementation(libs.firebase.storage)
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.boom))
    implementation(libs.facebook)

    implementation(libs.firebase)
    androidTestImplementation(libs.mokito.kotlin)
    androidTestImplementation(libs.android.test)
    androidTestImplementation(libs.mokito)
    androidTestImplementation(libs.unit)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
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
    implementation(libs.g.jetbrains.kotlinx)
    implementation(libs.androidx.retrofit)
    implementation(libs.androidx.converter.gson)
    implementation(libs.androidx.recycler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
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