package dependencies

object Versions {

    const val versionName = "2.13.0"

    const val targetSdk = 29
    const val minSdk = 9
    const val minSdkWithSupportLibrary = 14

    internal const val kotlin = "1.4.10"
    internal const val junit = "4.13.1"
    internal const val robolectric = "4.4"
    internal const val gson = "2.8.6"

    internal const val androidGradlePlugin = "4.1.0"
    internal const val bintrayGradlePlugin = "1.8.5"
    internal const val dokka = "1.4.10.2"

    internal const val jacoco = "0.8.6"
    internal const val mockk = "1.10.2"
    internal const val truth = "1.1"

    internal object AndroidX {
        internal const val annotation = "1.1.0"
        internal const val appCompat = "1.2.0"
        internal const val preference = "1.1.1"
        internal const val security = "1.0.0-rc03"
        internal const val startup = "1.0.0"
        internal const val liveData = "2.2.0"
        internal const val test = "1.3.0"
    }
}
