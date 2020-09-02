package dependencies

object Versions {

    const val versionName = "2.11.0"

    const val targetSdk = 29
    const val minSdk = 9
    const val minSdkWithSupportLibrary = 14

    internal const val kotlin = "1.3.72"
    internal const val junit = "4.12"
    internal const val robolectric = "4.4"
    internal const val gson = "2.8.6"

    internal const val androidGradlePlugin = "3.6.3"
    internal const val bintrayGradlePlugin = "1.8.5"
    internal const val androidMavenGradlePlugin = "2.1"
    internal const val dokkaGradlePlugin = "0.10.1"

    internal const val jacoco = "0.8.5"
    internal const val mockk = "1.9.3"
    internal const val truth = "1.0"

    internal object AndroidX {
        internal const val annotation = "1.1.0"
        internal const val appCompat = "1.1.0"
        internal const val preference = "1.1.1"
        internal const val security = "1.0.0-rc01"
        internal const val liveData = "2.2.0"
        internal const val test = "1.2.0"
    }
}
