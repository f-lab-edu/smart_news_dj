plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.ktlint)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.djyoo.smartnews"
    compileSdk {
        version =
            release(36) {
                minorApiLevel = 1
            }
    }

    defaultConfig {
        applicationId = "com.djyoo.smartnews"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // MockK 테스트 라이브러리 추가
    testImplementation(libs.test.mockk)

    // 코루틴 테스트가 필요한 경우를 대비해 추가 권장
    testImplementation(libs.kotlinx.coroutines.test)

    // 로컬 단위 테스트용 Robolectric 추가
    testImplementation(libs.robolectric)
}
