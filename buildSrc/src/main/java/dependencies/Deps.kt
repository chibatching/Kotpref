package dependencies

@Suppress("unused")
object Deps {

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    const val dokkaGradlePlugin =
        "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokka}"
    const val dokkaJava =
        "org.jetbrains.dokka:kotlin-as-java-plugin:${Versions.dokka}"

    const val junit = "junit:junit:${Versions.junit}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val truth = "com.google.truth:truth:${Versions.truth}"

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object Arch {
        const val liveData = "androidx.lifecycle:lifecycle-livedata:${Versions.AndroidX.liveData}"
        const val runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.AndroidX.liveData}"
    }

    object AndroidX {
        const val annotation = "androidx.annotation:annotation:${Versions.AndroidX.annotation}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}"
        const val preference = "androidx.preference:preference:${Versions.AndroidX.preference}"
        const val security = "androidx.security:security-crypto:${Versions.AndroidX.security}"
        const val startup = "androidx.startup:startup-runtime:${Versions.AndroidX.startup}"
        const val testCore = "androidx.test:core:${Versions.AndroidX.test}"
    }
}
