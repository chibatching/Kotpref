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
        val liveData = "android.arch.lifecycle:livedata:${Versions.arch}"
    }

    object SupportLibrary {
        val supportV4 = "com.android.support:support-v4:${Versions.supportLibrary}"
        val appCompatV7 = "com.android.support:appcompat-v7:${Versions.supportLibrary}"
        val preferenceV7 = "com.android.support:preference-v7:${Versions.supportLibrary}"
    }
}
