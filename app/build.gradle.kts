plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.example.appmvvm"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":mylibrary"))
    implementation("androidx.core:core-ktx:1.7.0")
    implementation( "androidx.appcompat:appcompat:1.4.1")
    implementation( "com.google.android.material:material:1.6.0")
    implementation( "androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation( "junit:junit:4.13.2")
    androidTestImplementation( "androidx.test.ext:junit:1.1.3")
    androidTestImplementation( "androidx.test.espresso:espresso-core:3.4.0")

    //Koin
    implementation( "io.insert-koin:koin-core:3.1.2")
    implementation( "io.insert-koin:koin-android:3.1.2")

    //Coroutines
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    //Glide
    implementation( "com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor( "com.github.bumptech.glide:compiler:4.13.0")

    //Room
    implementation( "androidx.room:room-runtime:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")
    implementation( "androidx.room:room-ktx:2.4.2")
}