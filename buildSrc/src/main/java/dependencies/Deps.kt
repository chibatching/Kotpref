package dependencies

object Deps {

    val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    val bintrayGradlePlugin = "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintrayGradlePlugin}"
    val androidMavenGradlePlugin = "com.github.dcendents:android-maven-gradle-plugin:${Versions.androidMavenGradlePlugin}"
    val dokkaAndroidGradlePlugin = "org.jetbrains.dokka:dokka-android-gradle-plugin:${Versions.dokkaAndroidGradlePlugin}"

    val junit = "junit:junit:${Versions.junit}"
    val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    val assertj = "org.assertj:assertj-core:${Versions.assertj}"

    val gson = "com.google.code.gson:gson:${Versions.gson}"

    object Kotlin {
        val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
        val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object Arch {
        val liveData = "androidx.lifecycle:lifecycle-livedata:${Versions.liveData}"
    }

    object AndroidX {
        val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}"
        val preference = "androidx.preference:preference:${Versions.AndroidX.preference}"
        val test = "androidx.test:core:${Versions.AndroidX.test}"
    }

    object Mockito {
        val core = "org.mockito:mockito-core:${Versions.mockito}"
        val kotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
    }
}
