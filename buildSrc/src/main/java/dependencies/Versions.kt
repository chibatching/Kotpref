package dependencies

object Versions {

    const val versionName = "2.13.2"

    const val targetSdk = 31
    const val minSdk = 9
    const val minSdkWithSupportLibrary = 14

    internal const val kotlin = "1.6.10"
    internal const val junit = "4.13.2"
    internal const val robolectric = "4.7.3"
    internal const val gson = "2.8.9"

    internal const val androidGradlePlugin = "7.0.4"
    internal const val dokka = "1.6.10"

    internal const val jacoco = "0.8.7"
    internal const val mockk = "1.12.2"
    internal const val truth = "1.1.3"

    internal object AndroidX {
        internal const val annotation = "1.3.0"
        internal const val appCompat = "1.4.0"
        internal const val preference = "1.1.1"
        internal const val security = "1.0.0"
        internal const val startup = "1.1.0"
        internal const val liveData = "2.4.0"
        internal const val test = "1.4.0"
    }
}
