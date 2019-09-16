package dependencies

object Deps {

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val bintrayGradlePlugin =
        "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintrayGradlePlugin}"
    const val androidMavenGradlePlugin =
        "com.github.dcendents:android-maven-gradle-plugin:${Versions.androidMavenGradlePlugin}"
    const val dokkaAndroidGradlePlugin =
        "org.jetbrains.dokka:dokka-android-gradle-plugin:${Versions.dokkaAndroidGradlePlugin}"

    const val junit = "junit:junit:${Versions.junit}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val truth = "com.google.truth:truth:${Versions.truth}"

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object Arch {
        const val liveData = "androidx.lifecycle:lifecycle-livedata:${Versions.liveData}"
        const val runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.liveData}"
    }

    object AndroidX {
        const val annotation = "androidx.annotation:annotation:${Versions.AndroidX.annotation}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}"
        const val preference = "androidx.preference:preference:${Versions.AndroidX.preference}"
        const val testCore = "androidx.test:core:${Versions.AndroidX.test}"
    }
}
