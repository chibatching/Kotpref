package com.chibatching.kotpref

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.manifest.AndroidManifest
import org.robolectric.res.Fs


class KotprefTestRunner(testClass: Class<*>) : RobolectricTestRunner(testClass) {

    override fun getAppManifest(config: Config): AndroidManifest {
        return AndroidManifest(
                Fs.fileFromPath("src/main/AndroidManifest.xml"),
                Fs.fileFromPath("build/intermediates/res/merged/androidTest/${BuildConfig.BUILD_TYPE}"),
                Fs.fileFromPath("build/intermediates/assets/merged/androidTest/${BuildConfig.BUILD_TYPE}")
        )
    }
}
